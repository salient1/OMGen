package com.omgen.generator;

import com.omgen.InvocationContext;

/**
 * Warning: generators that implement this interface should be stateless.
 */
public interface Generator {
    String generate(Class classToProcess, InvocationContext invocationContext) throws Exception;
}
