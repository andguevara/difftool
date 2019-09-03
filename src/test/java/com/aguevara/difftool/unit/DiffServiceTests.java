package com.aguevara.difftool.unit;


import com.aguevara.difftool.model.Result;
import com.aguevara.difftool.services.DiffResult;
import com.aguevara.difftool.services.DiffService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DiffServiceTests {

    @Autowired
    DiffService diffService;

    @Test
    public void testTwoEqualStrings() {
        final String diffLeft = "dGVzdA==";
        final String diffRight = "dGVzdA==";
        Result result = diffService.calculateDiff(diffLeft, diffRight);
        assertEquals("expected description is should be equal",
                "Strings are equal", result.getDescription());
        assertEquals("Expected type of result should be EQUAL", DiffResult.EQUAL, result.getResult());
    }

    @Test
    public void testTwoStringsDifferentSize() {
        final String diffLeft = "dGVzdA==";
        final String diffRight = "dGVzdDI=";
        Result result = diffService.calculateDiff(diffLeft, diffRight);
        assertEquals("expected description should be of different size",
                "Strings are not of equal size", result.getDescription());
        assertEquals("Expected type of result should be SIZE_NOT_EQUAL", DiffResult.SIZE_NOT_EQUAL,
                result.getResult());
    }

    @Test
    public void testTwoStringsSameSizeDifferentContent() {
        final String diffLeft = "dGVzdA==";
        final String diffRight = "dGV0cw==";
        Result result = diffService.calculateDiff(diffLeft, diffRight);
        assertEquals("expected description should be description of offset and difference",
                "The Strings differ and the first difference is at offset 2" +
                        " and the different characters are: ts", result.getDescription());
        assertEquals("Expected type of result should be DIFFERENT", DiffResult.DIFFERENT,
                result.getResult());
    }
}
