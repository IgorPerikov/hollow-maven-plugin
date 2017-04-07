package org.test;

import org.junit.Assert;
import org.junit.Test;

public class MyTest {

    @Test
    public void failedTest() {
        Assert.assertEquals(10, 1);
    }
}
