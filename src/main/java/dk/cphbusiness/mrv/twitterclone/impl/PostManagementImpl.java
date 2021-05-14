package dk.cphbusiness.mrv.twitterclone.impl;

import dk.cphbusiness.mrv.twitterclone.contract.PostManagement;
import dk.cphbusiness.mrv.twitterclone.dto.Post;
import dk.cphbusiness.mrv.twitterclone.dto.UserCreation;
import dk.cphbusiness.mrv.twitterclone.util.Time;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.*;
import java.util.stream.Collectors;

public class PostManagementImpl implements PostManagement {
    private Jedis jedis;
    private Time time;

    public PostManagementImpl(Jedis jedis, Time time) {
        this.jedis = jedis;
        this.time = time;
    }

    @Override
    public boolean createPost(String username, String message) {
        if(!jedis.hexists(username, "username")){
            return false;
        } else {
            jedis.sadd(username+":posts", message);
            jedis.sadd(username+":timeofpost",(String.valueOf(time.getCurrentTimeMillis())));
            return true;
        }
    }

    @Override
    public List<Post> getPosts(String username) {
        List<Post> allPosts = new ArrayList<Post>();
        List<String> strArray = new ArrayList();
        strArray.addAll(jedis.smembers(username+":posts"));
        for (int i = 0; i < strArray.size(); i++) {
            //Post post = new Post(time.getCurrentTimeMillis(), StrArray.get(i));
            allPosts.add(new Post(time.getCurrentTimeMillis(), strArray.get(i)));
            System.out.println(allPosts.get(i).message);
        }
        return allPosts;
    }

    @Override
    public List<Post> getPostsBetween(String username, long timeFrom, long timeTo) {

        List<Post> allPosts = new ArrayList<Post>();
        List<String> strArray = new ArrayList();
        List<Long> longArray = new ArrayList();
        strArray.addAll(jedis.smembers(username+":posts"));
        for(String s : jedis.smembers(username+":timeofpost")) longArray.add(Long.valueOf(s));
        for (int i = 0; i < strArray.size(); i++) {
                if(longArray.get(i) >= timeFrom && longArray.get(i) <= timeTo) {
                allPosts.add(new Post(time.getCurrentTimeMillis(), strArray.get(i)));
            }
        }
        return allPosts;
    }
}
