package com.bt;

/**
 * Created by burhan on 01.08.2015.
 */
public class Word {
    private String word;
    private int[] order;

    public int[] getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return order2str() + ", word : " + word;
    }

    private String order2str() {
        StringBuffer buffer = new StringBuffer("[");

        for(int index = 0; index < order.length; index++) {
            if(index > 0) {
                buffer.append(",");
            }

            buffer.append(order[index]);
        }

        buffer.append("]");
        return buffer.toString();
    }


    public void setOrder(int[] order) {
        this.order = order;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
