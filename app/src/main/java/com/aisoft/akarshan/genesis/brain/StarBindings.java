package com.aisoft.akarshan.genesis.brain;
/* Program AB Reference AIML 2.0 implementation
        Author:Akarshan Biswas
*/

/**
 * structure to hold binding of wildcards in input pattern, that pattern and topicpattern
 */
public class StarBindings {
    public Stars inputStars;
    public Stars thatStars;
    public Stars topicStars;
    /** Constructor  -- this class has public members
     *
     */
    public StarBindings () {
        inputStars = new Stars();
        thatStars = new Stars();
        topicStars = new Stars();
    }
}
