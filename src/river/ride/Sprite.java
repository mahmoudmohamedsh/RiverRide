package river.ride;


import javax.media.opengl.GL;
import java.lang.Math;

public class Sprite {
    private GL gl;
    private int x;
    private int y;
    private int texture;
    private float xScale;
    private float yScale;
    private int moveDirection;
    
    
    public Sprite(GL gl, int x, int y, int texture, float xScale, float yScale, int moveDirection) {
        this.gl = gl;
        this.x = x;
        this.y = y;
        this.xScale = xScale;
        this.yScale= yScale;
        this.texture = texture;
        this.moveDirection = moveDirection;
    }
    
    public void DrawSprite(){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture);	// Turn Blending On

        gl.glPushMatrix();
            gl.glTranslated( x/(consts.maxWidth/2.0)-1, y/(consts.maxHeight/2.0)-1, 0);
            gl.glScaled(xScale, yScale, 1);
            //System.out.println(x +" " + y);
            gl.glBegin(GL.GL_QUADS);
            // Front Face
                gl.glTexCoord2f(0.0f, 0.0f);
                gl.glVertex3f(-1.0f, -1.0f, -1.0f);
                gl.glTexCoord2f(1.0f, 0.0f);
                gl.glVertex3f(1.0f, -1.0f, -1.0f);
                gl.glTexCoord2f(1.0f, 1.0f);
                gl.glVertex3f(1.0f, 1.0f, -1.0f);
                gl.glTexCoord2f(0.0f, 1.0f);
                gl.glVertex3f(-1.0f, 1.0f, -1.0f);
            gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
        this.drawCircleColider();
        this.DrawRectColider();
    }
    
    
    public boolean colideWith(Sprite sprite){
        int [] value = this.clampFunction(sprite.getX(), sprite.getY());
        double distanece = Math.sqrt(Math.pow(sprite.getX() - value[0], 2) +Math.pow(sprite.getY() - value[1], 2) );
        gl.glColor3f(1.0f, 0.0f, 0.0f);
   gl.glBegin(GL.GL_LINES);
                       gl.glVertex2d(value[0]/(consts.maxWidth/2.0)-1, value[1]/(consts.maxHeight/2.0)-1 );
                         gl.glVertex2d(sprite.getX()/(consts.maxWidth/2.0)-1, sprite.getY()/(consts.maxHeight/2.0)-1 );

 gl.glEnd();
 gl.glColor3f(1.0f, 1.0f, 1.0f);
 if (distanece <= sprite.getxScale() * 50)
            gl.glColor3f(1.0f, 0.0f, 0.0f);
        
       return false;
   }
    public void drawCircleColider(){
        double x_c ,y_c;
        double ONE_DEGREE = (Math.PI / 180);
        double THREE_SIXTY = 2 * Math.PI;
        
         gl.glColor3f(0.0f, 1.0f, 0.0f);
        gl.glPushMatrix();
            gl.glTranslated( x/(consts.maxWidth/2.0)-1, y/(consts.maxHeight/2.0)-1, 0);
            gl.glScaled(xScale, yScale, 1);
            
            gl.glBegin(GL.GL_LINES);
            
//            gl.glBegin(GL.GL_POINT);
                for (double a = 0;a < THREE_SIXTY; a += ONE_DEGREE) {
                    x_c = (Math.cos(a));
                    y_c = (Math.sin(a));
                    gl.glVertex2d(x_c, y_c);
                }
       
            gl.glEnd();
            gl.glColor3f(1.0f, 1.0f, 1.0f);
            gl.glPopMatrix();
    }
    public void DrawRectColider(){
        

        gl.glPushMatrix();
            gl.glTranslated( x/(consts.maxWidth/2.0)-1, y/(consts.maxHeight/2.0)-1, 0);
            gl.glScaled(xScale, yScale, 1);
            gl.glColor3f(1.0f, 0.0f, 0.0f);
            gl.glBegin(GL.GL_LINES);
                gl.glVertex3f(-1.0f, -1.0f, -1.0f);
                gl.glVertex3f(1.0f, -1.0f, -1.0f);
                
                gl.glVertex3f(1.0f, -1.0f, -1.0f);
                gl.glVertex3f(1.0f, 1.0f, -1.0f);
                
                gl.glVertex3f(1.0f, 1.0f, -1.0f);
                gl.glVertex3f(-1.0f, 1.0f, -1.0f);
                
                gl.glVertex3f(-1.0f, 1.0f, -1.0f);
                gl.glVertex3f(-1.0f, -1.0f, -1.0f);
            gl.glEnd();
            
        gl.glPopMatrix();
        gl.glColor3f(1.0f, 1.0f, 1.0f);
    }
    public int[] clampFunction(int cx , int cy){
        int nx = (int) Math.max(this.x - this.xScale * 50 , Math.min(cx ,( this.x + this.xScale * 50)) );
        int ny = (int) Math.max(this.y - this.yScale * 50 , Math.min(cy ,( this.y + this.yScale * 50)) );
        return new int[]{nx,ny};
        
    }
    public void scale(float xScale, float yScale){
        
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setTexture(int texture) {
        this.texture = texture;
    }

    

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getTexture() {
        return texture;
    }

    public void setxScale(float xScale) {
        this.xScale = xScale;
    }

    public void setyScale(float yScale) {
        this.yScale = yScale;
    }

    public float getxScale() {
        return xScale;
    }

    public float getyScale() {
        return yScale;
    }

    
    public void setMoveDirection(int moveDirection) {
        this.moveDirection = moveDirection;
    }

    public int getMoveDirection() {
        return moveDirection;
    }


}
