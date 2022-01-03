package com.quasicontrol.wordle.core;

import java.util.List;

public record WordResult(
        String guess,
        Result[] results){
}
