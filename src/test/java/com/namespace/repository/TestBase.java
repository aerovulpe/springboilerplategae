package com.namespace.repository;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import org.junit.After;
import org.junit.Before;

import java.util.logging.Logger;


/**
 * All tests should extend this class to set up the GAE environment.
 * @see <a href="http://code.google.com/appengine/docs/java/howto/unittesting.html">Unit Testing in Appengine</a>
 */
public class TestBase
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(TestBase.class.getName());

	protected ObjectifyFactory objectifyFactory;
	
	private final LocalServiceTestHelper helper =
			new LocalServiceTestHelper(
					new LocalDatastoreServiceTestConfig(),
					new LocalMemcacheServiceTestConfig(),
					new LocalTaskQueueTestConfig()
				);

	@Before
	public void setUp()
	{
		this.helper.setUp();

		this.objectifyFactory = ObjectifyService.factory();

		/*
		 * Register your classes here or override this method,
		 * I always override this method and then I call this method using super.setUp()	
		 */
		//this.fact.register(Libro.class);
		
	}

	@After
	public void tearDown()
	{
		this.helper.tearDown();
	}
	
	/** 
	 * Utility methods that puts and immediately gets an entity 
	 */
	protected <T> T putAndGet(T saveMe)
	{
		Objectify ofy = this.objectifyFactory.begin();
		
		Key<T> key = ofy.save().entity(saveMe).now();

		T ent = ofy.load().key(key).now();
		System.out.println(ent);

		return ofy.load().entity(saveMe).now();
	}
}