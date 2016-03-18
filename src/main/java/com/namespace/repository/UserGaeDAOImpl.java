package com.namespace.repository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.Query;
import com.namespace.domain.Account;
import com.namespace.domain.UserGAE;
import com.namespace.util.WrappedBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserGaeDAOImpl implements UserGaeDAO {

    @Autowired
    private ObjectifyFactory objectifyFactory;

    private static final Logger logger = LoggerFactory.getLogger(UserGaeDAOImpl.class);

    public UserGaeDAOImpl() {
    }

    public UserGaeDAOImpl(ObjectifyFactory objectifyFactory) {
        this.objectifyFactory = objectifyFactory;
    }

    @Override
    public List<UserGAE> findAll() {
        Objectify ofy = objectifyFactory.begin();

        Query<UserGAE> q = ofy.load().type(UserGAE.class);

        return q.list();
    }

    @Override
    public List<UserGAE> findAllEnabledUsers(boolean isEnabled) {
        Objectify ofy = objectifyFactory.begin();

        Query<UserGAE> q = ofy.load().type(UserGAE.class).filter("enabled ==", isEnabled);

        List<UserGAE> users = q.list();

        logger.info("retrieving the users list from the datastore: "
                + users.toString());

        return users;
    }

    @Override
    public void create(UserGAE user) throws Exception {
        Objectify ofy = objectifyFactory.begin();

        if (user == null) {
            throw new Exception("You can't create a null user");

        } else if (ofy.load().key(Key.create(UserGAE.class, user.getUsername())).now() == null) {
            ofy.save().entity(user).now();
        } else {
            throw new Exception("User already exists.");
        }
    }

    @Override
    public boolean update(UserGAE user) {

        if (user == null)
            return false;

        Objectify ofy = objectifyFactory.begin();

        if (ofy.load().key(Key.create(UserGAE.class, user.getUsername())).now() != null) {
            ofy.save().entity(user).now();
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean remove(UserGAE user) {
        Objectify ofy = objectifyFactory.begin();
        ofy.delete().entity(user);
        return true;
    }

    @Override
    public UserGAE findByUsername(String username) {
        try {
            Objectify ofy = objectifyFactory.begin();
            return ofy.load().key(Key.create(UserGAE.class, username)).now();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void createUserAccount(final UserGAE user, final Account account) {

        final Objectify ofy = objectifyFactory.begin();
        ofy.transact(new Work<WrappedBoolean>() {
            @Override
            public WrappedBoolean run() {
                try {
                    logger.info("ofy.put(user) will be realized now");
                    ofy.save().entity(user).now();
                    logger.info("ofy.put(user) was realized sucessfully");

                    Key<UserGAE> userGaeKey = Key.create(UserGAE.class, user.getUsername());
                    logger.info("The username to be stored is:" + user.getUsername());
                    account.setUser(userGaeKey);
                    logger.info("ofy.put(account) will be realized now");
                    ofy.save().entity(user).now();
                    logger.info("ofy.put(account) was realized successfully");

                    return new WrappedBoolean(true);
                } catch (Exception e) {
                    logger.info("The transaction createUserAccount() failed");
                    logger.info("\nThe error: \n" + e.getMessage());
                    return new WrappedBoolean(false, "Unknown exception");
                }
            }
        });

        throw new UnsupportedOperationException();
    }

    @Override
    public UserGAE findByAccount(Account account) {
        Objectify ofy = this.objectifyFactory.begin();

        return ofy.load().key(account.getUser()).now();
    }

}

