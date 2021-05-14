package dk.cphbusiness.mrv.twitterclone;

import dk.cphbusiness.mrv.twitterclone.dto.UserCreation;
import dk.cphbusiness.mrv.twitterclone.impl.PostManagementImpl;
import dk.cphbusiness.mrv.twitterclone.impl.UserManagementImpl;
import dk.cphbusiness.mrv.twitterclone.util.TimeImpl;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class Main {

    private String host = "localhost";
    private int port = 6379;
    private GenericContainer redisContainer;
    private void setupContainer() {
        redisContainer = new GenericContainer(DockerImageName.parse("redis:alpine"))
                .withExposedPorts(6379);
        redisContainer.start();
        host = redisContainer.getHost();
        port = redisContainer.getFirstMappedPort();
    }

    public static void main(String[] args)throws Exception {

        /*
        Your task is to fill out the PostManagementImpl and UserManagementImpl classes.
        You must satisfy the unit tests, in order to complete it.

        Run the unit tests by right clicking the Java folder under Test,
        and choose Run All Tests (Ctrl+Shift+F10)
         */

        try{
            Jedis jedis = new Jedis("localhost",6379);
            System.out.println(jedis.ping());
            jedis.sadd("emil","post");
            System.out.println(jedis.smembers("emil"));
        }
        catch (Exception e){
            System.out.println(e);
        }


        }

    }

