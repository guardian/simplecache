package com.gu.cache.simplecache.statistics;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class StatisticsTest {
    @Test
    public void shouldCalculateHitRatioCorrectly() {
        assertThat(new Statistics(0, 0, 0, 1, 1, 1).getHitRatio(), is(Double.NaN));
        assertThat(new Statistics(0, 5, 0, 1, 1, 1).getHitRatio(), is(1.0));
        assertThat(new Statistics(0, 5, 5, 1, 1, 1).getHitRatio(), is(0.5));
        assertThat(new Statistics(0, 5, 15, 1, 1, 1).getHitRatio(), is(0.25));
    }
}
