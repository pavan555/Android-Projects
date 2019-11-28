package com.pavan.coin.man.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	private Texture background,coin,bomb;
	private Texture[] man;
	BitmapFont scoreText;


	private int manState=0,pause=0,gameState=0,coinCount,bombCount,score=0;
	float velocity=0f,gravity=0.2f,manY;

	Random random= new Random();
	ArrayList<Integer> coinX=new ArrayList<>();
	ArrayList<Integer> coinY=new ArrayList<>();
	ArrayList<Integer> bombX=new ArrayList<>();
	ArrayList<Integer> bombY=new ArrayList<>();


	ArrayList<Rectangle> coinPos = new ArrayList<>();
	ArrayList<Rectangle> bombPos = new ArrayList<>();
	Rectangle manPos;







	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man = new Texture[]{
				new Texture("frame-1.png"),
				new Texture("frame-2.png"),
				new Texture("frame-3.png"),
				new Texture("frame-4.png"),
				new Texture("frame-5.png"),
				new Texture("jump-up.png"),//6
				new Texture("jump-fall.png"),//7
				new Texture("dizzy-1.png")//8
		};
		coin=new Texture("coin.png");
		bomb=new Texture("bomb.png");
		scoreText = new BitmapFont();
		scoreText.setColor(Color.WHITE);
		scoreText.getData().setScale(10);

		manY=(float) Gdx.graphics.getHeight() / 2;


	}


	private void makeCoin(){
		float height=random.nextFloat() * Gdx.graphics.getHeight();
		if(height >= Gdx.graphics.getHeight() - coin.getHeight()) height = Gdx.graphics.getHeight() - coin.getHeight();
		coinY.add((int) height);
		coinX.add(Gdx.graphics.getWidth());
	}
	private void makeBomb() {
		float height=random.nextFloat() * Gdx.graphics.getHeight();
		if(height >= Gdx.graphics.getHeight() - bomb.getHeight()) height = Gdx.graphics.getHeight() - bomb.getHeight();
		bombY.add((int) height);
		bombX.add(Gdx.graphics.getWidth());
	}




	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());


		if(gameState == 0){
			//Game is About to Begin
			if(Gdx.input.justTouched()){
				gameState=1;
			}

		}else if(gameState == 1){
			//Game on
			//coins
			if(coinCount<100){
				coinCount++;
			}else {
				coinCount = 0;
				makeCoin();
			}

			coinPos.clear();
			for (int i = 0; i < coinX.size(); i++) {
				batch.draw(coin, coinX.get(i), coinY.get(i));
				coinX.set(i, coinX.get(i) - 4);
				coinPos.add(new Rectangle(coinX.get(i),coinY.get(i),coin.getWidth(),coin.getHeight()));
			}



			//bombs
			if(bombCount<500){
				bombCount++;
			}else{
				bombCount=0;
				makeBomb();
			}

			bombPos.clear();
			for (int i = 0; i < bombX.size(); i++) {
				batch.draw(bomb, bombX.get(i), bombY.get(i));
				bombX.set(i, bombX.get(i) - 8);
				bombPos.add(new Rectangle(bombX.get(i),bombY.get(i),bomb.getWidth(),bomb.getHeight()));
			}

			if(Gdx.input.justTouched()){
				velocity=-10;
				manState=5;
			}

			if(pause<10) pause++;
			else {
				pause = 0;
				manState = (manState + 1) % 5;
			}


			velocity+=gravity;
			manY -= velocity;
			if(manY <=0 ) manY =0;
			else if(manY >= Gdx.graphics.getHeight() - man[manState].getHeight()) manY = Gdx.graphics.getHeight() - man[manState].getHeight();


		}else if(gameState == 2){
			//game over
			manState=7;
			if(Gdx.input.justTouched()){
				gameState=1;
				score=0;
				coinX.clear();coinY.clear();
				bombX.clear();bombY.clear();
				coinCount=0;bombCount=0;
				coinPos.clear();bombPos.clear();

			}
		}


		batch.draw(man[manState], Gdx.graphics.getWidth() / 2 - man[manState].getWidth()/2, manY);
		manPos = new Rectangle(Gdx.graphics.getWidth() / 2 - man[manState].getWidth()/2, manY,man[manState].getWidth(),man[manState].getHeight());


		for(int i=0;i<coinPos.size();i++){
			if(Intersector.overlaps(manPos,coinPos.get(i))){
				score++;
				coinX.remove(i);
				coinY.remove(i);
				coinPos.remove(i);
				break;
			}
		}


		for(int i=0;i<bombPos.size();i++){
			if(Intersector.overlaps(manPos,bombPos.get(i))){
				gameState=2;
				manState=7;
			}
		}


		scoreText.draw(batch,String.format("%s",score),100,200);

		batch.end();
	}


	@Override
	public void dispose () {
		batch.dispose();

	}
}
