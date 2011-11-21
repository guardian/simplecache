package com.gu.cache.simplecache;

import static com.gu.option.Option.none;
import static com.gu.option.Option.some;
import com.gu.option.Option;

public class Clock {

    private static Option<Long> freeze = none();

    public static void freeze(long atTimeMillis) {
        freeze = some(atTimeMillis);
	}

    public static void freeze() {
        freeze(System.currentTimeMillis());
	}

    public static void unfreeze() {
        freeze = none();
	}

    public static long currentTimeMillis() {
        return freeze.getOrElse(System.currentTimeMillis());
	}

}
