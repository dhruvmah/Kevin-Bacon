import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class KBNumberTest {
	
	/*
	 * Instantiated only once for all tests. Make sure multiple calls to each
	 * method work as expected, and leftover data/variables from the last call
	 * is handled properly.
	 */
	KBNumber bacon = new KBNumber("movies_tiny.txt");
			
	@Before
	public void setUp() throws Exception {
		//nothing here.
	}

	@Test
	public void testCollaboration() {
		assertEquals(bacon.mostCollaboration(), 3);
	}
	
	@Test
	public void testCostars(){
		List<String> result = bacon.findCostars("C");
		assertEquals(result.size(), 3);
		assertTrue(result.contains("A"));
		assertTrue(result.contains("B"));
		assertTrue(result.contains("D"));
	}
	
	@Test
	public void testOneActor() {
		KBNumber bacon2 = new KBNumber("test1.txt");
		List<String> result = bacon2.findCostars("Bacon, Kevin");
		assertEquals(0, result.size());
		assertEquals(0, bacon2.mostCollaboration());
		assertEquals(0, bacon2.findBaconNumber("Bacon, Kevin"));	
	}
	
	@Test
	public void testNoKevinBaconInFile(){
		KBNumber bacon3 = new KBNumber("noKevinBacon.txt");
		assertEquals(-1, bacon3.findBaconNumber("A"));
	}
	
	@Test
	public void testFindBaconNumber(){
		int baconnum = bacon.findBaconNumber("N");
		int zero = bacon.findBaconNumber("Bacon, Kevin");
		assertEquals(zero, 0);
		assertEquals(baconnum, 6);
	}
		
	@Test
	public void testNoBaconPath () {
		KBNumber bacon4 = new KBNumber("noPath.txt");
		assertEquals(-1, bacon4.findBaconNumber("A"));
		assertEquals(null, bacon4.findBaconPath("C"));
	}
	
	
	@Test
	public void testFindBaconPath () {
		List<String> path = bacon.findBaconPath("D");
		assertEquals(path.size(), 5);
		Iterator<String> it = path.iterator();
		String s;
		s=it.next();
		assertEquals(s, "D");
		s=it.next(); 
		assertTrue(s.equals("Movie 2") || s.equals("Movie 7"));				
		s=it.next();
		assertEquals(s, "A");
		s=it.next();
		assertEquals(s, "Movie 0");
		s=it.next();
		assertEquals(s, "Bacon, Kevin");		
		
	}
}
