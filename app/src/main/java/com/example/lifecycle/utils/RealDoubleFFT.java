package com.example.lifecycle.utils;

/* renamed from: ca.uol.aig.fftpack.RealDoubleFFT */
public class RealDoubleFFT extends RealDoubleFFT_Mixed {
    private int ndim;
    public double norm_factor;
    private double[] wavetable;

    public RealDoubleFFT(int i) {
        this.ndim = i;
        this.norm_factor = (double) i;
        if (this.wavetable == null || this.wavetable.length != (this.ndim * 2) + 15) {
            this.wavetable = new double[((this.ndim * 2) + 15)];
        }
        rffti(this.ndim, this.wavetable);
    }

    /* renamed from: ft */
    public void mo6045ft(double[] dArr) {
        if (dArr.length == this.ndim) {
            rfftf(this.ndim, dArr, this.wavetable);
            return;
        }
        throw new IllegalArgumentException("The length of data can not match that of the wavetable");
    }

    /* renamed from: ft */
    public void mo6046ft(double[] dArr, Complex1D complex1D) {
        if (dArr.length == this.ndim) {
            rfftf(this.ndim, dArr, this.wavetable);
            if (this.ndim % 2 == 0) {
                complex1D.f32x = new double[((this.ndim / 2) + 1)];
                complex1D.f33y = new double[((this.ndim / 2) + 1)];
            } else {
                complex1D.f32x = new double[((this.ndim + 1) / 2)];
                complex1D.f33y = new double[((this.ndim + 1) / 2)];
            }
            complex1D.f32x[0] = dArr[0];
            complex1D.f33y[0] = 0.0d;
            for (int i = 1; i < (this.ndim + 1) / 2; i++) {
                int i2 = i * 2;
                complex1D.f32x[i] = dArr[i2 - 1];
                complex1D.f33y[i] = dArr[i2];
            }
            if (this.ndim % 2 == 0) {
                complex1D.f32x[this.ndim / 2] = dArr[this.ndim - 1];
                complex1D.f33y[this.ndim / 2] = 0.0d;
                return;
            }
            return;
        }
        throw new IllegalArgumentException("The length of data can not match that of the wavetable");
    }

    /* renamed from: bt */
    public void mo6044bt(double[] dArr) {
        if (dArr.length == this.ndim) {
            rfftb(this.ndim, dArr, this.wavetable);
            return;
        }
        throw new IllegalArgumentException("The length of data can not match that of the wavetable");
    }

    /* renamed from: bt */
    public void mo6043bt(Complex1D complex1D, double[] dArr) {
        if (this.ndim % 2 == 0) {
            if (complex1D.f32x.length != (this.ndim / 2) + 1) {
                throw new IllegalArgumentException("The length of data can not match that of the wavetable");
            }
        } else if (complex1D.f32x.length != (this.ndim + 1) / 2) {
            throw new IllegalArgumentException("The length of data can not match that of the wavetable");
        }
        dArr[0] = complex1D.f32x[0];
        for (int i = 1; i < (this.ndim + 1) / 2; i++) {
            int i2 = i * 2;
            dArr[i2 - 1] = complex1D.f32x[i];
            dArr[i2] = complex1D.f33y[i];
        }
        if (this.ndim % 2 == 0) {
            dArr[this.ndim - 1] = complex1D.f32x[this.ndim / 2];
        }
        rfftb(this.ndim, dArr, this.wavetable);
    }
}