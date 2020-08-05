package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import java.util.ArrayList;
import java.util.Random;

import javax.swing.JColorChooser;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	int manState = 0;
	int pause = 0;
	float gravity = 0.2f, velocity = 0;
	int manY = 0;
	Rectangle manRectangle;
	BitmapFont font;
	int score=0;
	int gamestate=0;
	Random rand;
	ArrayList<Integer> coinXs = new ArrayList<Integer>();
	ArrayList<Integer> coinYx = new ArrayList<Integer>();
	ArrayList<Rectangle> coinRectangle=new ArrayList<>();
	ArrayList<Integer> bombXs = new ArrayList<Integer>();
	ArrayList<Integer> bombYx = new ArrayList<Integer>();
    ArrayList<Rectangle> bombRectangle=new ArrayList<>();

	Texture coin,dizzy;
	int coinCount;
	Texture bomb;
	int bombCount;

	@Override
	public void create() {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man = new Texture[4];
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");
		manY = Gdx.graphics.getHeight() / 2;
		coin = new Texture("coin.png");
		dizzy=new Texture("dizzy-1.png");
		bomb = new Texture("bomb.png");
		rand = new Random();
		font=new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

	}

	public void makecoin() {
		float height = rand.nextFloat() * Gdx.graphics.getHeight();
		coinYx.add((int) height);
		coinXs.add(Gdx.graphics.getWidth());
	}

	public void makebomb() {


		float height1 = rand.nextFloat() * Gdx.graphics.getHeight();
		bombXs.add(Gdx.graphics.getWidth());
		bombYx.add((int) height1);

	}

	@Override
	public void render () {
batch.begin();
batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
if(gamestate==1)
{
	if(coinCount<100)
	{
		coinCount++;
	}
	else
	{
		coinCount=0;
		makecoin();
	}
	if(bombCount<250)
	{
		bombCount++;
	}
	else
	{
		bombCount=0;
		makebomb();
	}
	coinRectangle.clear();
	for(int i=0;i<coinXs.size();i++)
	{
		batch.draw(coin,coinXs.get(i),coinYx.get(i));
		coinXs.set(i,coinXs.get(i)-4);
		coinRectangle.add(new Rectangle(coinXs.get(i),coinYx.get(i),coin.getWidth(),coin.getHeight()));

	}
	bombRectangle.clear();
	for(int i=0;i<bombXs.size();i++)
	{
		batch.draw(bomb,bombXs.get(i),bombYx.get(i));
		bombXs.set(i,bombXs.get(i)-8);
		bombRectangle.add(new Rectangle(bombXs.get(i),bombYx.get(i),bomb.getWidth(),bomb.getHeight()));
	}
	if(Gdx.input.justTouched())
		velocity=-10;
	if(pause<8)
	{
		pause++;
	}
	else {
		pause=0;

		if (manState < 3) {
			manState++;
		} else {
			manState = 0;
		}
	}
	velocity+=gravity;
	manY-=velocity;
	if(manY<=0)
		manY=0;
}
else if(gamestate==0)
		{
if(Gdx.input.justTouched())
	gamestate=1;


		}
else if(gamestate==2)
		{
			if(Gdx.input.justTouched())
				gamestate=1;
            manY = Gdx.graphics.getHeight() / 2;
            score=0;
            velocity=0;
            coinXs.clear();
            coinYx.clear();
            bombXs.clear();
            bombYx.clear();
            coinRectangle.clear();

            coinCount=0;
            bombRectangle.clear();
            bombCount=0;
		}
if(gamestate==2)
{
	batch.draw(dizzy,Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
}
else {
	batch.draw(man[manState], Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
}
manRectangle= new Rectangle(Gdx.graphics.getWidth()/2-man[manState].getWidth()/2,manY,man[manState].getWidth(),man[manState].getHeight());
for(int i=0;i<coinRectangle.size();i++)
{
    if(Intersector.overlaps(manRectangle,coinRectangle.get(i)))
    {
       // Gdx.app.log("coin!","collision");
		score++;
		coinRectangle.remove(i);
		coinXs.remove(i);
		coinYx.remove(i);
		break;
    }
}
        for(int i=0;i<bombRectangle.size();i++)
        {
            if(Intersector.overlaps(manRectangle,bombRectangle.get(i)))
            {
                //Gdx.app.log("bomb!","collision");
				gamestate=2;

            }
        }
        font.draw(batch,String.valueOf(score),100,200);
batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
