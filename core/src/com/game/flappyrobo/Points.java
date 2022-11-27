package com.game.flappyrobo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;


public class Points {
    private int points = 0;
    public int width, height;

    private int PADDING = 5;
    private IntArray numbers;

    private Array<Texture> textures = new Array<>();
    private Texture buffer;

    public Points(){
        for (int i = 0; i < 10; i++)
            textures.add(FlappyRobo.assets.loadTexture(i + ".png"));
        numbers = new IntArray();
        setPoints(0);
    }

    public void draw(SpriteBatch batch, float x, float y){
        for (int i = 0; i < numbers.size; i++) {
            buffer = textures.get(numbers.get(i));
            batch.draw(buffer, x + (buffer.getWidth() + PADDING) * i, y);
        }

    }

    public void setPoints(int points) {
        this.points = points;
        numbers.clear();

        width = 0;
        if (points == 0){
            numbers.add(0);
            width += textures.get(0).getWidth() + PADDING;
            height = textures.get(0).getHeight();
        }

        for (int i = points; i > 0; i /= 10){
            System.out.println("points: " + i % 10);
            numbers.add(i % 10);
            width += textures.get(i % 10).getWidth() + PADDING;
            height = textures.get(i % 10).getHeight();
        }

        numbers.reverse();


    }

    public int getPoints() {
        return points;
    }
}
