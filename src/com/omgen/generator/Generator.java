package com.omgen.generator;

/**
 * Warning: generators that implement this interface should be stateless.
 */
public interface Generator {
    String generate(Class classToProcess) throws Exception;
}
