package dk.cphbusiness.mrv.twitterclone.impl;

import dk.cphbusiness.mrv.twitterclone.contract.UserManagement;
import dk.cphbusiness.mrv.twitterclone.dto.Post;
import dk.cphbusiness.mrv.twitterclone.dto.UserCreation;
import dk.cphbusiness.mrv.twitterclone.dto.UserOverview;
import dk.cphbusiness.mrv.twitterclone.dto.UserUpdate;
import dk.cphbusiness.mrv.twitterclone.util.Time;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.*;

public class UserManagementImpl implements UserManagement {

    private Jedis jedis;

    public UserManagementImpl(Jedis jedis) {
        this.jedis = jedis;
    }

    @Override
    public boolean createUser(UserCreation userCreation) {
        if(jedis.exists(userCreation.username)){
            return false;
        }

        HashMap<String, String> test = new HashMap<String, String>();
        test.put("username",userCreation.username);
        test.put("password",userCreation.passwordHash);
        test.put("firstname",userCreation.firstname);
        test.put("lastname",userCreation.lastname);
        test.put("birthday",userCreation.birthday);
        test.put(":followscount",userCreation.followsCount);
        test.put(":followercount",userCreation.followerCount);
        jedis.hmset(userCreation.username, test);
        jedis.save();
        return true;
    }

    @Override
    public UserOverview getUserOverview(String username) {
        UserOverview overview = null;
        Map<String,String> userInfo = new HashMap<>();
        if(!jedis.hexists(username,"username")){
            return null;
        }
        try (Transaction trans = jedis.multi()) {
            trans.exists(username);
            var user = trans.hgetAll(username);
            trans.exec();
            trans.save();
            userInfo = user.get();
        }catch (Exception ex){
            ex.getMessage();
            return null;
        }
        overview = new UserOverview(userInfo.get(username), userInfo.get("firstname"), userInfo.get("lastname"),userInfo.get("birthday")
                ,Long.parseLong(userInfo.get(":followscount")),Long.parseLong(userInfo.get(":followercount")));
        jedis.close();

        if(userInfo.isEmpty()){
            return null;
        }
        return overview;
    }

    @Override
    public boolean updateUser(UserUpdate userUpdate) {
        if(!jedis.exists(userUpdate.username)){
            return false;
        }
        //var user =  getUserOverview(userUpdate.username);
        HashMap<String, String> test = new HashMap<String, String>();
            if(userUpdate.firstname != null) test.put("firstname",userUpdate.firstname);
            if(userUpdate.lastname != null) test.put("lastname",userUpdate.lastname);
            if(userUpdate.birthday != null) test.put("birthday",userUpdate.birthday);
            jedis.hmset(userUpdate.username, test);
            jedis.save();
            return true;

    }

    @Override
    public boolean followUser(String username, String usernameToFollow) {
        if(!jedis.hexists(usernameToFollow,"username")||!jedis.hexists(username,"username")){
            return false;
        }
        else {
            UserOverview uo = getUserOverview(username);
            jedis.sadd(username+":follows",usernameToFollow);
            jedis.sadd(usernameToFollow+":following",usernameToFollow);
            uo.numFollowers = jedis.hincrBy( username,":followercount",1);
            uo.numFollowing = jedis.hincrBy(usernameToFollow,":followscount",1);
            return true;
        }
    }

    @Override
    public boolean unfollowUser(String username, String usernameToUnfollow) {
        if (!jedis.hexists(usernameToUnfollow, "username")||!jedis.hexists(username, "username" )) {
            return false;
        }
        else {
            UserOverview uo = getUserOverview(username);
            jedis.srem(username + ":follows", usernameToUnfollow);
            jedis.srem(usernameToUnfollow + ":following",username );
            uo.numFollowing = jedis.hincrBy(username, ":followscount",-1);
            uo.numFollowing = jedis.hincrBy(usernameToUnfollow, ":followercount",-1);
            return true;


        }
    }

    @Override
    public Set<String> getFollowedUsers(String username) {
        if (!jedis.hexists(username,"username")) {
            return null;
        } else {
            Set<String> follows = jedis.smembers(username+":follows");

            return follows;

        }
    }

    @Override
    public Set<String> getUsersFollowing(String username) {
        if (!jedis.hexists(username,"username")) {
            return null;
        } else {
            Set<String> following = jedis.smembers(username + ":following");
            return following;
        }
    }

}
