package com.seatgeek.sixpack;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ExperimentTest {
    @Mock Sixpack mockSixpack;

    @Mock OnParticipationSuccess mockSuccess;

    @Mock OnParticipationFailure mockFailure;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void testGetSixpack() {
        Alternative test = new Alternative("test");
        String name = "test-experiment";

        Experiment experiment = new ExperimentBuilder(mockSixpack)
                .withAlternative(test)
                .withName(name)
                .build();

        assertEquals(mockSixpack, experiment.sixpack);
    }

    @Test
    public void testGetName() {
        String name = "test-experiment";
        Alternative test = new Alternative("test");

        Experiment experiment = new ExperimentBuilder(mockSixpack)
                .withName(name)
                .withAlternative(test)
                .build();

        assertEquals(name, experiment.name);
    }

    @Test(expected = NoExperimentNameException.class)
    public void testEmptyNameThrows() {
        String name = "";

        new ExperimentBuilder(mockSixpack)
                .withName(name)
                .build();
    }

    @Test(expected = NoExperimentNameException.class)
    public void testNullNameThrows() {
        new ExperimentBuilder(mockSixpack)
                .withName(null)
                .build();
    }

    @Test(expected = NoExperimentNameException.class)
    public void testNoNameThrows() {
        new ExperimentBuilder(mockSixpack)
                .build();
    }

    @Test
    public void testGetAlternatives() {
        String name = "test-experiment";
        Alternative one = new Alternative("one");
        Alternative two = new Alternative("two");
        Alternative three = new Alternative("three");

        Experiment experimentA = new ExperimentBuilder(mockSixpack)
                .withName(name)
                .withAlternative(one)
                .withAlternative(two)
                .withAlternative(three)
                .build();

        Experiment experimentB = new ExperimentBuilder(mockSixpack)
                .withName(name)
                .withAlternatives(one, two, three)
                .build();

        Set<Alternative> setOfAlternatives = new HashSet<Alternative>();
        setOfAlternatives.add(one);
        setOfAlternatives.add(two);
        setOfAlternatives.add(three);

        Experiment experimentC = new ExperimentBuilder(mockSixpack)
                .withName(name)
                .withAlternatives(setOfAlternatives)
                .build();

        assertEquals(setOfAlternatives, experimentA.alternatives);
        assertEquals(setOfAlternatives, experimentB.alternatives);
        assertEquals(setOfAlternatives, experimentC.alternatives);
    }

    @Test(expected = NoAlternativesException.class)
    public void testNoAlternativesThrows() {
        String name = "test-experiment";
        new ExperimentBuilder(mockSixpack)
                .withName(name)
                .build();
    }

    @Test(expected = NoAlternativesException.class)
    public void testEmptyAlternativesThrows() {
        String name = "test-experiment";
        Set<Alternative> emptyAlternatives = new HashSet<Alternative>();

        new ExperimentBuilder(mockSixpack)
                .withName(name)
                .withAlternatives(emptyAlternatives)
                .build();
    }

    @Test(expected = NoAlternativesException.class)
    public void testNullAlternativesThrows() {
        String name = "test-experiment";

        new ExperimentBuilder(mockSixpack)
                .withName(name)
                .withAlternatives()
                .build();
    }

    @Test
    public void testGetForcedChoice() {
        String name = "test-experiment";
        Alternative test = new Alternative("test");

        Experiment experiment = new ExperimentBuilder(mockSixpack)
                .withName(name)
                .withAlternative(test)
                .withForcedChoice(test)
                .build();

        assertTrue(experiment.hasForcedChoice());
        assertEquals(test, experiment.forcedChoice);
    }


    @Test
    public void testNoForcedChoice() {
        String name = "test-experiment";
        Alternative test = new Alternative("test");

        Experiment experiment = new ExperimentBuilder(mockSixpack)
                .withName(name)
                .withAlternative(test)
                .build();

        assertFalse(experiment.hasForcedChoice());
        assertNull(experiment.forcedChoice);
    }

    @Test
    public void testGetTrafficFraction() {
        String name = "test-experiment";
        Alternative test = new Alternative("test");
        Double fraction = .5d;

        Experiment experiment = new ExperimentBuilder(mockSixpack)
                .withName(name)
                .withAlternative(test)
                .withTrafficFraction(fraction)
                .build();

        assertEquals(fraction, experiment.trafficFraction);
    }

    @Test(expected = BadTrafficFractionException.class)
    public void testLessThanZeroTrafficFractionThrows() {
        Double fraction = -.5d;

        new ExperimentBuilder(mockSixpack)
                .withTrafficFraction(fraction);
    }

    @Test(expected = BadTrafficFractionException.class)
    public void testGreaterThanOneTrafficFractionThrows() {
        Double fraction = 2d;

        new ExperimentBuilder(mockSixpack)
                .withTrafficFraction(fraction);
    }

    @Test
    public void testToStringEqualsExperimentName() {
        String name = "test-experiment";
        Alternative test = new Alternative("test");

        Experiment experiment = new ExperimentBuilder(mockSixpack)
                .withName(name)
                .withAlternative(test)
                .build();

        assertEquals(name, experiment.toString());
    }

    @Test
    public void testParticipateCallsSixpackParticipate() {
        String name = "test-experiment";
        Alternative test = new Alternative("test");

        Experiment experiment = new ExperimentBuilder(mockSixpack)
                .withName(name)
                .withAlternative(test)
                .build();

        experiment.participate(mockSuccess, mockFailure);

        verify(mockSixpack).participateIn(experiment, mockSuccess, mockFailure);
    }
}
