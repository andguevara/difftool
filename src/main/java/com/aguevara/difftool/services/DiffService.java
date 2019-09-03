package com.aguevara.difftool.services;

import com.aguevara.difftool.model.Result;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class DiffService {

    public Result calculateDiff(String diffLeft, String diffRight) {
        diffLeft = new String(Base64.getDecoder().decode(diffLeft));
        diffRight = new String(Base64.getDecoder().decode(diffRight));
        if (diffLeft.equals(diffRight)) {
            return new Result("Strings are equal", DiffResult.EQUAL);
        } else if (diffLeft.length() != diffRight.length()) {
            return new Result("Strings are not of equal size", DiffResult.SIZE_NOT_EQUAL);
        } else {
            int offset = -1;
            StringBuilder difference = new StringBuilder();
            for (int i = 0; i < diffLeft.length(); i++) {
                if (diffLeft.charAt(i) != diffRight.charAt(i)) {
                    if (offset == -1) {
                        offset = i;
                    }
                    difference.append(diffRight.charAt(i));
                }
            }
            return new Result("The Strings differ and the first difference is at offset " + offset +
                    " and the different characters are: " + difference.toString(), DiffResult.DIFFERENT);
        }
    }
}
