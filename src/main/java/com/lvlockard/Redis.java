package com.lvlockard;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

public class Redis {

    private static Jedis jedis = new Jedis("localhost");

    public static void testServerConnection() {
        System.out.println("Server is running: " + jedis.ping());
    }

    public static void createUser(String email, String password) {
        // add user email to SET of users
        jedis.sadd("users", email);
        // add user info to HASH of user:<email>
        Map<String, String> fields = new HashMap<>();
        fields.put("email", email);
        fields.put("password", password);
        jedis.hmset("user:" + email, fields);
    }

    public static Boolean loginUser(String email, String password) {
        String hKey = "user" + email;
        return (
            jedis.exists(hKey) &&
            email.equals(jedis.hget(hKey, "email")) &&
            password.equals(jedis.hget(hKey, "password"))
        );

    }

    public static void enterAddress(String userEmail, Entry entry) {
        // add entry email to SET of the user's contact's emails, addressbook:<userEmail>
        jedis.sadd("addressbook:" + userEmail, entry.getEmail());
        // add entry info to HASH of contacts for the user, entry:<userEmail>:<email>
        Map<String, String> fields = new HashMap<>();
        fields.put("firstName", entry.getFirstName());
        fields.put("lastName", entry.getLastName());
        fields.put("email", entry.getEmail());
        fields.put("address", entry.getAddress());
        fields.put("phoneNumber", entry.getPhoneNumber());
        jedis.hmset("entry:" + userEmail + ":" + entry.getEmail(), fields);
    }


}
