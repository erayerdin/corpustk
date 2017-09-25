package com.erayerdin.corpustk.models.corpus;

import com.erayerdin.corpustk.models.Model;
import com.erayerdin.corpustk.models.graphology.GraphSet;
import com.erayerdin.corpustk.models.graphology.GraphSetFactory;
import com.erayerdin.linglib.graphology.GraphemeType;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CorpusTest {
    private Corpus corpus;
    private GraphSet graphSet;
    private Text[] texts;

    @Before
    public void setUp() throws Exception {
        /////////////7
        // GraphSet //
        //////////////
        this.graphSet = GraphSetFactory.createGraphSet(
                "abcdefghijklmnopqrstuvwxyz",
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                GraphemeType.NUMERIC, GraphemeType.STOP, GraphemeType.LINKING
        );

        //////////
        // Text //
        //////////
        this.texts = new Text[] {
                new Text("Quisque quis accumsan nunc. Duis sit amet mi non neque gravida varius. Suspendisse arcu ligula, vestibulum ac dui in, dictum dapibus nulla. Pellentesque ornare maximus porta. Etiam bibendum lacus odio, sit amet vestibulum est consectetur eu. Mauris a vestibulum lorem. Nunc malesuada vitae purus elementum convallis. Etiam sed ligula mattis, gravida justo eu, commodo tellus. Nullam et semper justo. Vivamus scelerisque accumsan mi, non pharetra enim dignissim vitae. Duis aliquet mollis erat in venenatis. Nunc dictum elementum risus, et vehicula turpis pretium a. Ut nec odio hendrerit nunc faucibus bibendum.", this.graphSet),
                new Text("Praesent nec ligula sodales, euismod justo quis, aliquet turpis. Praesent vitae erat id enim tristique fermentum. Maecenas mollis tortor vitae efficitur congue. Praesent nec lacinia lacus, eget lobortis libero. Duis tempor ullamcorper eros a rhoncus. Sed mollis massa eu risus accumsan, quis iaculis risus gravida. Cras condimentum vulputate nibh nec interdum. Ut commodo est ut viverra finibus.", this.graphSet),
                new Text("Interdum et malesuada fames ac ante ipsum primis in faucibus. In facilisis posuere porta. Curabitur at auctor quam. Donec pulvinar volutpat consectetur. Curabitur in convallis massa. In feugiat ante enim. Praesent ac tellus mi. Vestibulum vitae efficitur nisl. Nulla sed felis eget massa efficitur molestie in nec mi. Vivamus dignissim sollicitudin lectus, id posuere est vestibulum id. In hac habitasse platea dictumst. Praesent dapibus odio vel tempor eleifend.", this.graphSet)
        };

        ////////////
        // Corpus //
        ////////////
        this.corpus = new Corpus("A Random Corpus", this.graphSet);
        this.corpus.getTexts().addAll(this.texts);
    }

    @Test
    public void step2_readObject() throws Exception {
        Corpus corpus = (Corpus) Model.load(new File("temp/random.crp"));
        assertEquals(
                "A Random Corpus",
                corpus.getTitle()
        );
        assertEquals(
                this.texts[0].getContent(),
                corpus.getTexts().get(0).getContent()
        );
    }

    @Test
    public void step1_writeObject() throws Exception {
        Model.save(this.corpus, new File("temp/random.crp"));
    }

}