package me.asakura_kukii.siegecore.util.math;

public class PAxis implements Cloneable{

    private PVector o = new PVector(0, 0, 0);
    private PVector x = new PVector(1, 0, 0);
    private PVector y = new PVector(0, 1, 0);
    private PVector z = new PVector(0, 0, 1);

    public PVector getO() {
        return o;
    }

    public void setO(PVector o) {
        this.o = o.clone();
    }

    public PVector getX() {
        return x;
    }

    public void setX(PVector x) {
        this.x = x.clone();
    }

    public PVector getY() {
        return y;
    }

    public void setY(PVector y) {
        this.y = y.clone();
    }

    public PVector getZ() {
        return z;
    }

    public void setZ(PVector z) {
        this.z = z.clone();
    }

    public PAxis() {}

    public PAxis(PVector o, PVector x, PVector y, PVector z) {
        this.o = o.clone();
        this.x = (PVector) x.clone().normalize();
        this.y = (PVector) y.clone().normalize();
        this.z = (PVector) z.clone().normalize();
    }

    public PAxis(PVector o, PVector z, float yaw) {
        this.o = o.clone();
        this.z = (PVector) z.clone().normalize();
        this.x = new PVector(-PMath.sin(yaw + PMath.pi / 2), 0F, PMath.cos(yaw + PMath.pi / 2));
        this.y = (PVector) this.x.clone().cross(this.z);
    }

    public PAxis translate(PVector pV) {
        return translate(pV.x, pV.y, pV.z);
    }

    public PAxis translate(float x, float y, float z) {
        this.o = this.getAbsolute(x, y, z);
        return this;
    }

    public PAxis rotateX(float angle) {
        this.y.rotateAxis(angle, this.x.x, this.x.y, this.x.z);
        this.z.rotateAxis(angle, this.x.x, this.x.y, this.x.z);
        return this;
    }

    public PAxis rotateY(float angle) {
        this.x.rotateAxis(angle, this.y.x, this.y.y, this.y.z);
        this.z.rotateAxis(angle, this.y.x, this.y.y, this.y.z);
        return this;
    }

    public PAxis rotateZ(float angle) {
        this.x.rotateAxis(angle, this.z.x, this.z.y, this.z.z);
        this.y.rotateAxis(angle, this.z.x, this.z.y, this.z.z);
        return this;
    }

    public PVector getAbsolute(PVector pV) {
        return getAbsolute(pV.x, pV.y, pV.z);
    }

    public PVector getAbsolute(float x, float y, float z) {
        return (PVector) this.x.clone().mul(x).add(this.y.clone().mul(y)).add(this.z.clone().mul(z)).add(this.o);
    }

    @Override
    public PAxis clone() {
        try {
            PAxis clone = (PAxis) super.clone();
            clone.o = this.o.clone();
            clone.x = this.x.clone();
            clone.y = this.y.clone();
            clone.z = this.z.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
