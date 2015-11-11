package myappconverter.dragnddrop.scenes;

import com.myappconverter.java.coregraphics.CGGeometry;
import com.myappconverter.java.coregraphics.CGPoint;
import com.myappconverter.java.coregraphics.CGSize;
import com.myappconverter.java.foundations.NSArray;
import com.myappconverter.java.foundations.NSMutableArray;
import com.myappconverter.java.foundations.NSSet;
import com.myappconverter.java.foundations.NSString;
import com.myappconverter.java.spritekit.SKAction;
import com.myappconverter.java.spritekit.SKLabelNode;
import com.myappconverter.java.spritekit.SKNode;
import com.myappconverter.java.spritekit.SKScene;
import com.myappconverter.java.spritekit.SKSpriteNode;
import com.myappconverter.uikit.myappclasses.MyAppTouch;

import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MainScene extends SKScene {



    private String kAnimalNodeName  = "movable";
    private String kHeroNodeName    = "hero";
    private String kCardNodeName    = "card";
    private String kRestartNodeName = "restart";

    SKSpriteNode background;
    SKLabelNode label, labelResult, score;
    SKSpriteNode selectedNode;


    String imageNames[];
    CGPoint imagePositions[];
    SKSpriteNode heros[];
    SKSpriteNode cards[];

    SKSpriteNode restart;


    int iscore;



    @Override
    public SKScene initWithSize(CGSize _size) {
        super.initWithSize(_size);

        background = SKSpriteNode.spriteNodeWithImageNamed(SKSpriteNode.class, new NSString("blue-shooting-stars"));
        selectedNode = new SKSpriteNode();
        label = SKLabelNode.labelNodeWithFontNamed(SKLabelNode.class, new NSString("font.ttf"));
        label.setColor(Color.WHITE);
        label.setPosition(CGPoint.make(getSize().width / 2, getSize().height - label.getSize().height - 50));
        label.setFontSize(60);
        label.setText(new NSString("Start"));

        labelResult = SKLabelNode.labelNodeWithFontNamed(SKLabelNode.class, new NSString("font.ttf"));
        labelResult.setColor(Color.GREEN);
        labelResult.setPosition(CGPoint.make(getSize().width / 2, getSize().height - label.getSize().height - 40));
        labelResult.setFontSize(50);
        labelResult.setText(new NSString(""));

        score = SKLabelNode.labelNodeWithFontNamed(SKLabelNode.class, new NSString("font.ttf"));
        score.setColor(Color.BLACK);
        score.setPosition(CGPoint.make(getSize().width / 2, 20));
        score.setFontSize(30);
        score.setText(new NSString("Score : 00"));



        restart = SKSpriteNode.spriteNodeWithImageNamed(SKSpriteNode.class, new NSString("restart"));
        restart.setPosition(CGPoint.make(getFrame().getSize().getWidth() / 2, getSize().getHeight() / 2));
        restart.setZOrder(100);
        restart.setName(kRestartNodeName);
        restart.setHidden(true);
        this.addChild(restart);

        // 1
        this.background.setName("background");
        this.background.setAnchorPoint(CGPoint.make(0, 0));
        this.background.setZOrder(-100);

        // 2
        this.addChild(background);
        this.addChild(label);
        this.addChild(labelResult);
        this.addChild(score);

        // 3
        imageNames = new String[]{"bird", "cat", "dog", "turtle"};
        heros = new SKSpriteNode[4];
        imagePositions = new CGPoint[4];
        cards = new SKSpriteNode[4];

        for (int i= 0; i<imageNames.length; i++){
            String imageName = imageNames[i];

            SKSpriteNode sprite = SKSpriteNode.spriteNodeWithImageNamed(SKSpriteNode.class, new NSString(imageName));
            sprite.setName(kAnimalNodeName + imageName);


            float offsetFraction = (float) ((i + 1.0)/((imageNames.length) + 1.0));
            sprite.setPosition(CGPoint.make(_size.width * offsetFraction, _size.height / 2));

            SKSpriteNode card = SKSpriteNode.spriteNodeWithColorSize(SKSpriteNode.class, Color.GREEN, sprite.getSize());
            card.setPosition(sprite.getPosition());
            card.setHidden(true);
            card.setName(kCardNodeName);


            imagePositions[i] = sprite.getPosition();
            heros[i] = sprite;
            cards[i] = card;

            this.addChild(sprite);
            this.addChild(card);
        }


        return this;
    }


    @Override
    public void touchesBeganWithEvent(NSSet<MyAppTouch> touches, MotionEvent event) {

        MyAppTouch touch = (MyAppTouch)touches.anyObject();
        CGPoint positionInScene = touch.locationInNode(this);


        runAction(SKAction.playSoundFileNamedWaitForCompletion(new NSString("birdsound.mp3"), true));
        SKNode touchedNode = this.nodeAtPoint(positionInScene);
        if(touchedNode instanceof SKLabelNode){

            label.setText(new NSString("Where is the bird ?"));
            hideAnimals();

        }
        selectNodeForTouch(positionInScene);
    }

    float degToRad(double degree) {
        return (float)(degree / 180.0 * Math.PI);
    }

    void selectNodeForTouch(CGPoint touchLocation) {
        // 1
        SKNode touchedNode = this.nodeAtPoint(touchLocation);

        if (touchedNode instanceof SKSpriteNode) {
            // 2
            if (!selectedNode.isEqual(touchedNode)) {
                selectedNode.removeAllActions();
                selectedNode.runAction(SKAction.rotateToAngleDuration(0.0f, 0.1f));
                selectedNode =  (SKSpriteNode)touchedNode;
                NSString name =  selectedNode.getName();

            if(name.getWrappedString().startsWith(kAnimalNodeName)){

                    NSMutableArray<SKAction> list = new NSMutableArray<SKAction>();
                    list.addObject(SKAction.rotateByAngleDuration(degToRad(-4.0), 0.1));
                    list.addObject(SKAction.rotateByAngleDuration(0.0f, 0.1f));
                    list.addObject(SKAction.rotateByAngleDuration(degToRad(4.0), 0.1));
                    SKAction sequence = SKAction.sequence(list);
                    selectedNode.runAction(SKAction.repeatActionForever(sequence));
                    showAnimals();
                    if(selectedNode.getName().getWrappedString().replace(kAnimalNodeName,"").equals("bird")){
                        labelResult.setText(new NSString("Great"));
                        restart.setHidden(false);
                        iscore+=1;
                        score.setText(new NSString("Score : "+iscore));
                    }
                    else{
                        labelResult.setText(new NSString("Sorry, try again"));
                        restart.setHidden(false);
                    }


            }if(name.getWrappedString().startsWith(kRestartNodeName)){

                   restart();
                }


            }
        }
    }


    void hideAnimals(){


        for(int i=0; i < heros.length ;i++){

            heros[i].setHidden(true);
            cards[i].setHidden(false);

        }

    }

    void showAnimals(){


        for(int i=0; i < heros.length ;i++){

            heros[i].setHidden(false);
            cards[i].setHidden(true);

        }

    }


    void restart(){


        restart.setHidden(true);
        labelResult.setText(new NSString(""));

        shuffleArray();

        for (int i= 0; i<imageNames.length; i++){

            heros[i].removeFromParent();
            cards[i].removeFromParent();

            String imageName = imageNames[i];

            SKSpriteNode sprite = SKSpriteNode.spriteNodeWithImageNamed(SKSpriteNode.class, new NSString(imageName));
            sprite.setName(kAnimalNodeName + imageName);


            float offsetFraction = (float) ((i + 1.0)/((imageNames.length) + 1.0));
            sprite.setPosition(CGPoint.make(getSize().width * offsetFraction, getSize().height / 2));

            SKSpriteNode card = SKSpriteNode.spriteNodeWithColorSize(SKSpriteNode.class, Color.GREEN, sprite.getSize());
            card.setPosition(sprite.getPosition());
            card.setHidden(true);
            card.setName(kCardNodeName);


            imagePositions[i] = sprite.getPosition();
            heros[i] = sprite;
            cards[i] = card;

            this.addChild(sprite);
            this.addChild(card);
        }

        hideAnimals();
    }

    void shuffleArray()
    {



        Random rnd = new Random();
        for (int i = imageNames.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            String a = imageNames[index];
            imageNames[index] = imageNames[i];
            imageNames[i] = a;
        }
    }



}