package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.StringUtils;

public class StringUtilsTest {

    @Test
    void checkEmptyOrNull(){
        Assertions.assertFalse(StringUtils.checkNullOrEmpty(""));
        Assertions.assertTrue(StringUtils.checkNullOrEmpty("sd"));
    }
}
