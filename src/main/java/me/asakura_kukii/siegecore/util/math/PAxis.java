package me.asakura_kukii.siegecore.util.math;

public class PAxis implements Cloneable{

    public PVector pVX = new PVector(1, 0, 0);
    public PVector pVY = new PVector(0, 1, 0);
    public PVector pVZ = new PVector(0, 0, 1);

    public PAxis() {}

    public PAxis(PVector x, PVector y, PVector z) {
        this.pVX = (PVector) x.normalize();
        this.pVY = (PVector) y.normalize();
        this.pVZ = (PVector) z.normalize();
    }

    public PAxis(PVector z, float yaw) {
        this.pVZ = (PVector) z.normalize();
        this.pVX = new PVector(-PMath.sin(yaw + PMath.pi / 2), 0F, PMath.cos(yaw + PMath.pi / 2));
        this.pVY = (PVector) this.pVZ.clone().cross(this.pVX);
    }

    public PAxis rotateX(float angle) {
        PAxis pA = this.clone();
        pA.pVY.rotateAxis(angle, pA.pVX.x, pA.pVX.y, pA.pVX.z);
        pA.pVZ.rotateAxis(angle, pA.pVX.x, pA.pVX.y, pA.pVX.z);
        return pA;
    }

    public PAxis rotateY(float angle) {
        PAxis pA = this.clone();
        pA.pVX.rotateAxis(angle, pA.pVY.x, pA.pVY.y, pA.pVY.z);
        pA.pVZ.rotateAxis(angle, pA.pVY.x, pA.pVY.y, pA.pVY.z);
        return pA;
    }

    public PAxis rotateZ(float angle) {
        PAxis pA = this.clone();
        pA.pVX.rotateAxis(angle, pA.pVZ.x, pA.pVZ.y, pA.pVZ.z);
        pA.pVY.rotateAxis(angle, pA.pVZ.x, pA.pVZ.y, pA.pVZ.z);
        return pA;
    }

    @Override
    public PAxis clone() {
        try {
            PAxis clone = (PAxis) super.clone();
            clone.pVX = this.pVX.clone();
            clone.pVY = this.pVY.clone();
            clone.pVZ = this.pVZ.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
