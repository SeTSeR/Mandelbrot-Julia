package sample;

/**
 * Created by hp on 27.09.2016.
 */
public class Complex {

    private Double real;
    private Double imagine;

    public Complex(Double re, Double im) {
        this.real = re;
        this.imagine = im;
    }

    public Complex(Double x0) {
        this(x0, 0.0);
    }

    public Double getReal() {
        return real;
    }

    public Double getImagine() {
        return imagine;
    }

    public Complex conjugate() {
        return new Complex(this.getReal(), -this.getImagine());
    }

    public Complex divide(Double x) {
        return new Complex(this.getReal() / x, this.getImagine() / x);
    }

    public Double abs() {
        return Math.sqrt(this.getReal() * this.getReal() + this.getImagine() * this.getImagine());
    }

    public Complex add(Complex other) {
        return new Complex(this.getReal() + other.getReal(), this.getImagine() + other.getImagine());
    }

    public Complex substract(Complex other) {
        return new Complex(this.getReal() - other.getReal(), this.getImagine() - other.getImagine());
    }

    public Complex multiply(Complex other) {
        return new Complex(this.getReal() * other.getReal() - this.getImagine() * other.getImagine(),
                           this.getReal() * other.getImagine() + this.getImagine() * other.getReal());
    }

    public Complex divide(Complex other) {
        return multiply(other.conjugate()).divide(other.getReal() * other.getReal() + other.getImagine() * other.getImagine());
    }

    public Complex power(Integer pow) {
        if(pow == 0) return new Complex(1.0);
        Complex z0 = new Complex(1.0);
        if(pow % 2 == 1) z0 = multiply(z0);
        Complex z1 = power(pow/2);
        z0 = z0.multiply(z1);
        z0 = z0.multiply(z1);
        return z0;
    }

    public void print() {
        System.out.print(real);
        if(imagine > -0.000001) System.out.print("+");
        System.out.print(imagine);
        System.out.println("*i");
    }
}
