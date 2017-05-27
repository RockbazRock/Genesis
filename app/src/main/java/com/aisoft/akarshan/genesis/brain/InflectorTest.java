package com.aisoft.akarshan.genesis.brain;

import junit.framework.TestCase;
import org.junit.Test;
// http://stackoverflow.com/questions/4757800/configuring-intellij-idea-for-unit-testing-with-junit
/**
 * Created by Akarshan on 5/11/17.
 */
public class InflectorTest extends TestCase {
    @Test
    public void testPluralize() throws Exception {
        Inflector inflector = new Inflector();
        String pairs[][] = {{"dog","dogs"},{"person","people"},{"cats","cats"}};
        for (int i = 0; i < pairs.length; i++) {
        String singular = pairs[i][0];
        String expected = pairs[i][1];
        String actual = inflector.pluralize(singular);
        assertEquals("Pluralize "+pairs[0][0],expected, actual);
        }


    }

    @Test
    public void testSingularize() throws Exception {

    }
}
