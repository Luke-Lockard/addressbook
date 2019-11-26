package com.lvlockard.utils;

/**
 * Establish connection to redis database. Holds all the methods involving the database.
 *
 * DB structure:
 *
 * A SET of users:
 *  KEY:    'users'
 *  VALUES: <user_email>...
 *
 * holds the emails of each user (which are unique)
 *
 *
 * A HASH for each user's information:
 *  KEY:    'user:<user_email>'
 *  FIELDS: 'email', 'password', 'salt'
 *
 * for login & identification, created on registration
 * 1 hash per user
 *
 *
 * A SET for each user's contacts:
 *  KEY:    'addressbook:<user_email>'
 *  VALUES: <contact_email>...
 *
 * Each user has a set holding all the emails of their contacts.
 * 1 hash per user
 * Useful for pulling all the user's contact's hashes
 *
 *
 * A HASH for each contact's info, for each user
 *  KEY:    'entry:<user_email>:<contact_email>
 *  FIELDS: 'firstName', 'lastName', 'email', 'address', 'phoneNumber'
 *
 * For each value in every 'addressbook:<user_email>' there is a hash holding
 * the information of that contact
 *
 */

import redis.clients.jedis.Jedis;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Redis {

    private static Jedis jedis = new Jedis("localhost");

    /**
     * Pings the server
     */
    public static void testServerConnection() {
        System.out.println("Server is running: " + jedis.ping());
    }

    /**
     * Add user email to set of users. Encrypts the password and makes a hash for that user with its email and encrypted password.
     *
     * @param email
     * @param passwordToHash
     * @return true if successful, false if email is taken or encryption fails
     */
    public static boolean createUser(String email, String passwordToHash) {
        // Check if the user already exists
        // Could check if ISMEMBER of users, or if user:<userEmail> exists
        if (!jedis.exists("user:" + email)) {
            // Hash the password with SHA-256
            String securePassword;
            byte [] salt;
            try {
                salt = SHAhash.getSalt();
                securePassword = SHAhash.get_SHA_256_SecurePassword(passwordToHash, salt);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return false;
            }

            System.out.println(securePassword);
            String saltString = Base64.getEncoder().encodeToString(salt);
            System.out.println(saltString);

            // add user email to SET of users
            jedis.sadd("users", email);
            // add user info to HASH of user:<userEmail>
            Map<String, String> fields = new HashMap<>();
            fields.put("email", email);
            fields.put("password", securePassword);
            fields.put("salt", saltString);

            jedis.hmset("user:" + email, fields);
            return true;
        } else {
            return false;
        }
    }


    /**
     * Encrypts the password and checks it against the user hash's password
     *
     * @param email
     * @param passwordToHash
     * @return true if successful, false if encryption fails or login credentials are wrong
     */
    public static boolean loginUser(String email, String passwordToHash) {
        // check if user exists
        String hKey = "user:" + email;
        if (jedis.exists(hKey)){
            // Get the salt from the user's hash
            String saltString = jedis.hget(hKey, "salt");
            System.out.println("salt: " + saltString);

            byte [] salt = Base64.getDecoder().decode(saltString);

            String securePassword = SHAhash.get_SHA_256_SecurePassword(passwordToHash, salt);
            System.out.println(securePassword);

            return (securePassword.equals(jedis.hget(hKey, "password")));
        } else {
            System.out.println("user:" + email + " doesn't exist");
            return false;
        }
    }


    /**
     * Add a contact entry to the user's address book. Adds the email to set of user contacts 'addressbook'
     * Create hash with entry info specific to the user and the contact
     *
     * @param userEmail
     * @param entry
     */
    public static boolean enterAddress(String userEmail, Entry entry) {

        if (entry.getFirstName() == null || entry.getLastName() == null || entry.getEmail() == null || entry.getAddress() == null || entry.getPhoneNumber() == null)
            return false;

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

        return true;
    }


    /**
     * Get the list of user's contacts emails from the addressbook set
     *
     * @param userEmail
     * @return List contactList
     */
    public static String[] getUserContacts(String userEmail) {
//        List<String> contactList = new ArrayList<>();
        Set contactSet = jedis.smembers("addressbook:" + userEmail);
//        contactList.addAll(contactSet);
        String[] contactArray = new String[contactSet.size()];
        contactSet.toArray(contactArray);
        System.out.println("Contents of Set ::" + contactSet);

        for (int i=0; i<contactArray.length; i++)
            System.out.println("Element at the index " + (i + 1) + " is ::" + contactArray[i]);

        return contactArray;
    }


    /**
     * Gets all the contact info for each contact for a user
     *
     * @param userEmail
     * @param contacts
     * @return list of Entry objects containing the contact info
     */
    public static Entry[] getUserEntries(String userEmail, String[] contacts) {
        Entry[] entries = new Entry[contacts.length];

        String hKey = "entry:" + userEmail;

        for (int i = 0; i < entries.length; i++) {
            Map<String, String> result = jedis.hgetAll(hKey + ":" + contacts[i]);
            Entry entry = new Entry(
                    result.get("firstName"),
                    result.get("lastName"),
                    result.get("email"),
                    result.get("address"),
                    result.get("phoneNumber")
            );
            entries[i] = entry;
        }

        return entries;
    }

    /**
     * Delete a user's contact from the DB
     *
     * @param userEmail
     * @param contactEmail
     * @return true if successful, false if not
     */
    public static boolean deleteEntry(String userEmail, String contactEmail) {
        String hKey = "entry:" + userEmail + ":" + contactEmail;

        // TODO: if one of the DB actions fails, rollback any changes that have been made.
        if (jedis.exists(hKey)) {
            // Remove from SET addressbook:<userEmail> & HASH entry:<userEmail>:<contactEmail>
            jedis.srem("addressbook:" + userEmail, contactEmail);
            Long result = jedis.del(hKey);
            return result == 1;
        } else {
            return false;
        }
    }


    /**
     * Update a contact's information
     *
     * @param userEmail
     * @param contactEmail
     * @param newEntry
     */
    public static void updateEntry(String userEmail, String contactEmail, Entry newEntry) {
        String hKey = "entry:" + userEmail + ":" + contactEmail;

        // if email hasn't changed, we can just update the other values
        if (contactEmail.equals(newEntry.getEmail())) {
            Map<String, String> fields = new HashMap<>();
            fields.put("firstName", newEntry.getFirstName());
            fields.put("lastName", newEntry.getLastName());
            fields.put("address", newEntry.getAddress());
            fields.put("phoneNumber", newEntry.getPhoneNumber());
            jedis.hmset(hKey, fields);
        }
        // If email has changed, then the hKey will be different. So we delete the old and just create the new
        else {
            deleteEntry(userEmail, contactEmail);
            enterAddress(userEmail, newEntry);
        }
    }
}
