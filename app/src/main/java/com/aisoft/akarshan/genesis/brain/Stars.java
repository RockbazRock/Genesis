package com.aisoft.akarshan.genesis.brain;
/* Program AB Reference AIML 2.0 implementation
        Author:Akarshan Biswas
*/
import java.util.ArrayList;


/**
 * Array of values matching wildcards
 */
public class Stars extends ArrayList<String> {
    public String star (int i) {
        if (i < size())
        return get(i);
        else return null;
    }

}
