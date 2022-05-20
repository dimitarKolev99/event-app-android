package com.example.myfirstapp.model;

public interface UsersTableMethods {

    /**
     * insert a User into the Users table
     * with the given User object
     * @param user
     */
    void insert(User user);


    /**
     * update a User in the Users table
     * with the given User object
     * @param user
     */
    void update(User user);


    /**
     * delete a User in the Users table
     * with the given User object
     * @param user
     */
    void delete(User user);

}
