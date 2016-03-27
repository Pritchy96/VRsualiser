attribute vec4 a_Position;
attribute vec4 a_Color;
uniform mat4 u_MVP;

varying vec4 v_Color;

void main()
{
    v_Color = a_Color;
    gl_Position = a_Position  * u_MVP;
}