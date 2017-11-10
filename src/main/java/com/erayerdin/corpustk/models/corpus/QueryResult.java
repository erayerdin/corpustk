package com.erayerdin.corpustk.models.corpus;

import com.erayerdin.linglib.corpus.Token;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ToString(exclude = {"results"})
public class QueryResult {
    @Getter private String query;
    @Getter private Token[] tokens;
    @Getter private GramType type;
    @Getter @Setter private int size = 1;

    public QueryResult(String query, Token[] tokens, GramType type) {
        this.query = query;
        this.tokens = tokens;
        this.type = type;
    }

    public QueryResult(String query, Token[] tokens, GramType type, int size) {
        this.query = query;
        this.tokens = tokens;
        this.type = type;
        this.size = size;
    }

    public void incrementSize() {
        this.size++;
    }

    public boolean equals(Token[] tokens) {
        if (tokens.length != this.getTokens().length) return false;

        for (int i = 0 ; i < tokens.length ; i++) {
            Token outerToken = tokens[i];
            Token innerToken = this.getTokens()[i];

            if (innerToken != null && outerToken != null) {
                if (!innerToken.lowerize().equals(outerToken.lowerize()) && !innerToken.capitalize().equals(outerToken.capitalize()))
                    return false;
            } else if (innerToken != outerToken) {
                return false;
            }
        }

        return true;
    }
}
