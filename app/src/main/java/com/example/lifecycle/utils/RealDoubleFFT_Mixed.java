package com.example.lifecycle.utils;

/* renamed from: ca.uol.aig.fftpack.RealDoubleFFT_Mixed */
class RealDoubleFFT_Mixed {
    RealDoubleFFT_Mixed() {
    }

    /* access modifiers changed from: package-private */
    public void radf2(int i, int i2, double[] dArr, double[] dArr2, double[] dArr3, int i3) {
        int i4 = i;
        int i5 = i2;
        for (int i6 = 0; i6 < i5; i6++) {
            int i7 = i6 * 2;
            int i8 = i6 * i4;
            int i9 = (i6 + i5) * i4;
            dArr2[i7 * i4] = dArr[i8] + dArr[i9];
            dArr2[(((i7 + 1) * i4) + i4) - 1] = dArr[i8] - dArr[i9];
        }
        if (i4 >= 2) {
            if (i4 != 2) {
                for (int i10 = 0; i10 < i5; i10++) {
                    for (int i11 = 2; i11 < i4; i11 += 2) {
                        int i12 = i4 - i11;
                        int i13 = (i11 - 2) + i3;
                        int i14 = i11 - 1;
                        int i15 = (i10 + i5) * i4;
                        int i16 = i14 + i15;
                        int i17 = i14 + i3;
                        int i18 = i15 + i11;
                        double d = (dArr3[i13] * dArr[i16]) + (dArr3[i17] * dArr[i18]);
                        double d2 = (dArr3[i13] * dArr[i18]) - (dArr3[i17] * dArr[i16]);
                        int i19 = i10 * 2;
                        int i20 = i19 * i4;
                        int i21 = i10 * i4;
                        int i22 = i11 + i21;
                        dArr2[i11 + i20] = dArr[i22] + d2;
                        int i23 = (i19 + 1) * i4;
                        dArr2[i12 + i23] = d2 - dArr[i22];
                        int i24 = i20 + i14;
                        int i25 = i14 + i21;
                        dArr2[i24] = dArr[i25] + d;
                        dArr2[(i12 - 1) + i23] = dArr[i25] - d;
                    }
                }
                if (i4 % 2 == 1) {
                    return;
                }
            }
            for (int i26 = 0; i26 < i5; i26++) {
                int i27 = i26 * 2;
                int i28 = i4 - 1;
                dArr2[(i27 + 1) * i4] = -dArr[((i26 + i5) * i4) + i28];
                dArr2[(i27 * i4) + i28] = dArr[i28 + (i26 * i4)];
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void radb2(int i, int i2, double[] dArr, double[] dArr2, double[] dArr3, int i3) {
        int i4 = i;
        int i5 = i2;
        for (int i6 = 0; i6 < i5; i6++) {
            int i7 = i6 * 2;
            int i8 = i7 * i4;
            int i9 = (i4 - 1) + ((i7 + 1) * i4);
            dArr2[i6 * i4] = dArr[i8] + dArr[i9];
            dArr2[(i6 + i5) * i4] = dArr[i8] - dArr[i9];
        }
        if (i4 >= 2) {
            if (i4 != 2) {
                for (int i10 = 0; i10 < i5; i10++) {
                    for (int i11 = 2; i11 < i4; i11 += 2) {
                        int i12 = i4 - i11;
                        int i13 = i11 - 1;
                        int i14 = i10 * i4;
                        int i15 = i10 * 2;
                        int i16 = i15 * i4;
                        int i17 = i13 + i16;
                        int i18 = (i15 + 1) * i4;
                        int i19 = (i12 - 1) + i18;
                        dArr2[i13 + i14] = dArr[i17] + dArr[i19];
                        double d = dArr[i17] - dArr[i19];
                        int i20 = i16 + i11;
                        int i21 = i12 + i18;
                        dArr2[i14 + i11] = dArr[i20] - dArr[i21];
                        double d2 = dArr[i20] + dArr[i21];
                        int i22 = (i10 + i5) * i4;
                        int i23 = i13 + i22;
                        int i24 = (i11 - 2) + i3;
                        int i25 = i13 + i3;
                        dArr2[i23] = (dArr3[i24] * d) - (dArr3[i25] * d2);
                        dArr2[i22 + i11] = (dArr3[i24] * d2) + (dArr3[i25] * d);
                    }
                }
                if (i4 % 2 == 1) {
                    return;
                }
            }
            for (int i26 = 0; i26 < i5; i26++) {
                int i27 = i4 - 1;
                int i28 = i26 * 2;
                dArr2[(i26 * i4) + i27] = dArr[(i28 * i4) + i27] * 2.0d;
                dArr2[i27 + ((i26 + i5) * i4)] = dArr[(i28 + 1) * i4] * -2.0d;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void radf3(int i, int i2, double[] dArr, double[] dArr2, double[] dArr3, int i3) {
        int i4 = i;
        int i5 = i2;
        int i6 = i3 + i4;
        for (int i7 = 0; i7 < i5; i7++) {
            int i8 = (i7 + i5) * i4;
            int i9 = ((i5 * 2) + i7) * i4;
            double d = dArr[i8] + dArr[i9];
            int i10 = i7 * 3;
            int i11 = i7 * i4;
            dArr2[i10 * i4] = dArr[i11] + d;
            dArr2[(i10 + 2) * i4] = (dArr[i9] - dArr[i8]) * 0.866025403784439d;
            dArr2[(i4 - 1) + ((i10 + 1) * i4)] = dArr[i11] + (d * -0.5d);
        }
        if (i4 != 1) {
            for (int i12 = 0; i12 < i5; i12++) {
                for (int i13 = 2; i13 < i4; i13 += 2) {
                    int i14 = i4 - i13;
                    int i15 = i13 - 2;
                    int i16 = i15 + i3;
                    int i17 = i13 - 1;
                    int i18 = (i12 + i5) * i4;
                    int i19 = i17 + i18;
                    int i20 = i17 + i3;
                    int i21 = i13 + i18;
                    double d2 = (dArr3[i16] * dArr[i19]) + (dArr3[i20] * dArr[i21]);
                    double d3 = (dArr3[i16] * dArr[i21]) - (dArr3[i20] * dArr[i19]);
                    int i22 = i15 + i6;
                    int i23 = ((i5 * 2) + i12) * i4;
                    int i24 = i17 + i23;
                    int i25 = i17 + i6;
                    int i26 = i23 + i13;
                    double d4 = (dArr3[i22] * dArr[i24]) + (dArr3[i25] * dArr[i26]);
                    double d5 = (dArr3[i22] * dArr[i26]) - (dArr3[i25] * dArr[i24]);
                    double d6 = d2 + d4;
                    double d7 = d3 + d5;
                    int i27 = i12 * 3;
                    int i28 = i27 * i4;
                    int i29 = i12 * i4;
                    int i30 = i17 + i29;
                    dArr2[i17 + i28] = dArr[i30] + d6;
                    int i31 = i13 + i29;
                    dArr2[i13 + i28] = dArr[i31] + d7;
                    double d8 = dArr[i30] + (d6 * -0.5d);
                    double d9 = dArr[i31] + (d7 * -0.5d);
                    double d10 = (d3 - d5) * 0.866025403784439d;
                    double d11 = (d4 - d2) * 0.866025403784439d;
                    int i32 = (i27 + 2) * i4;
                    dArr2[i17 + i32] = d8 + d10;
                    int i33 = (i27 + 1) * i4;
                    dArr2[(i14 - 1) + i33] = d8 - d10;
                    dArr2[i32 + i13] = d9 + d11;
                    dArr2[i14 + i33] = d11 - d9;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void radb3(int i, int i2, double[] dArr, double[] dArr2, double[] dArr3, int i3) {
        int i4 = i;
        int i5 = i2;
        int i6 = i3 + i4;
        for (int i7 = 0; i7 < i5; i7++) {
            int i8 = i7 * 3;
            double d = dArr[(i4 - 1) + ((i8 + 1) * i4)] * 2.0d;
            int i9 = i8 * i4;
            double d2 = dArr[i9] + (-0.5d * d);
            dArr2[i7 * i4] = dArr[i9] + d;
            double d3 = dArr[(i8 + 2) * i4] * 1.732050807568878d;
            dArr2[(i7 + i5) * i4] = d2 - d3;
            dArr2[((i5 * 2) + i7) * i4] = d2 + d3;
        }
        if (i4 != 1) {
            for (int i10 = 0; i10 < i5; i10++) {
                for (int i11 = 2; i11 < i4; i11 += 2) {
                    int i12 = i4 - i11;
                    int i13 = i11 - 1;
                    int i14 = i10 * 3;
                    int i15 = (i14 + 2) * i4;
                    int i16 = i13 + i15;
                    int i17 = (i14 + 1) * i4;
                    int i18 = (i12 - 1) + i17;
                    double d4 = dArr[i16] + dArr[i18];
                    int i19 = i14 * i4;
                    int i20 = i13 + i19;
                    double d5 = dArr[i20] + (d4 * -0.5d);
                    int i21 = i10 * i4;
                    dArr2[i13 + i21] = dArr[i20] + d4;
                    int i22 = i15 + i11;
                    int i23 = i12 + i17;
                    double d6 = dArr[i22] - dArr[i23];
                    int i24 = i19 + i11;
                    double d7 = dArr[i24] + (d6 * -0.5d);
                    dArr2[i11 + i21] = dArr[i24] + d6;
                    double d8 = (dArr[i16] - dArr[i18]) * 0.866025403784439d;
                    double d9 = (dArr[i22] + dArr[i23]) * 0.866025403784439d;
                    double d10 = d5 - d9;
                    double d11 = d5 + d9;
                    double d12 = d7 + d8;
                    double d13 = d7 - d8;
                    int i25 = (i10 + i5) * i4;
                    int i26 = i11 - 2;
                    int i27 = i26 + i3;
                    int i28 = i13 + i3;
                    dArr2[i13 + i25] = (dArr3[i27] * d10) - (dArr3[i28] * d12);
                    dArr2[i25 + i11] = (dArr3[i27] * d12) + (dArr3[i28] * d10);
                    int i29 = ((i5 * 2) + i10) * i4;
                    int i30 = i13 + i29;
                    int i31 = i26 + i6;
                    int i32 = i13 + i6;
                    dArr2[i30] = (dArr3[i31] * d11) - (dArr3[i32] * d13);
                    dArr2[i29 + i11] = (dArr3[i31] * d13) + (dArr3[i32] * d11);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void radf4(int i, int i2, double[] dArr, double[] dArr2, double[] dArr3, int i3) {
        int i4 = i;
        int i5 = i2;
        int i6 = i3 + i4;
        int i7 = i6 + i4;
        for (int i8 = 0; i8 < i5; i8++) {
            int i9 = (i8 + i5) * i4;
            int i10 = ((i5 * 3) + i8) * i4;
            double d = dArr[i9] + dArr[i10];
            int i11 = i8 * i4;
            int i12 = (i8 + (i5 * 2)) * i4;
            double d2 = dArr[i11] + dArr[i12];
            int i13 = i8 * 4;
            dArr2[i13 * i4] = d + d2;
            int i14 = i4 - 1;
            dArr2[i14 + ((i13 + 3) * i4)] = d2 - d;
            dArr2[i14 + ((i13 + 1) * i4)] = dArr[i11] - dArr[i12];
            dArr2[(i13 + 2) * i4] = dArr[i10] - dArr[i9];
        }
        if (i4 >= 2) {
            if (i4 != 2) {
                for (int i15 = 0; i15 < i5; i15++) {
                    for (int i16 = 2; i16 < i4; i16 += 2) {
                        int i17 = i4 - i16;
                        int i18 = i16 - 2;
                        int i19 = i18 + i3;
                        int i20 = i16 - 1;
                        int i21 = (i15 + i5) * i4;
                        int i22 = i20 + i21;
                        int i23 = i20 + i3;
                        int i24 = i16 + i21;
                        double d3 = (dArr3[i19] * dArr[i22]) + (dArr3[i23] * dArr[i24]);
                        double d4 = (dArr3[i19] * dArr[i24]) - (dArr3[i23] * dArr[i22]);
                        int i25 = i18 + i6;
                        int i26 = (i15 + (i5 * 2)) * i4;
                        int i27 = i20 + i26;
                        int i28 = i20 + i6;
                        int i29 = i16 + i26;
                        double d5 = (dArr3[i25] * dArr[i27]) + (dArr3[i28] * dArr[i29]);
                        double d6 = (dArr3[i25] * dArr[i29]) - (dArr3[i28] * dArr[i27]);
                        int i30 = i18 + i7;
                        int i31 = ((i5 * 3) + i15) * i4;
                        int i32 = i20 + i31;
                        int i33 = i20 + i7;
                        int i34 = i31 + i16;
                        double d7 = (dArr3[i30] * dArr[i32]) + (dArr3[i33] * dArr[i34]);
                        double d8 = (dArr3[i30] * dArr[i34]) - (dArr3[i33] * dArr[i32]);
                        double d9 = d3 + d7;
                        double d10 = d7 - d3;
                        double d11 = d4 + d8;
                        double d12 = d4 - d8;
                        int i35 = i15 * i4;
                        int i36 = i16 + i35;
                        double d13 = dArr[i36] + d6;
                        double d14 = dArr[i36] - d6;
                        int i37 = i20 + i35;
                        double d15 = dArr[i37] + d5;
                        double d16 = dArr[i37] - d5;
                        int i38 = i15 * 4;
                        int i39 = i38 * i4;
                        dArr2[i20 + i39] = d9 + d15;
                        int i40 = i17 - 1;
                        int i41 = (i38 + 3) * i4;
                        dArr2[i40 + i41] = d15 - d9;
                        dArr2[i16 + i39] = d11 + d13;
                        dArr2[i17 + i41] = d11 - d13;
                        int i42 = (i38 + 2) * i4;
                        dArr2[i20 + i42] = d12 + d16;
                        int i43 = (i38 + 1) * i4;
                        dArr2[i40 + i43] = d16 - d12;
                        dArr2[i42 + i16] = d10 + d14;
                        dArr2[i17 + i43] = d10 - d14;
                    }
                }
                if (i4 % 2 == 1) {
                    return;
                }
            }
            for (int i44 = 0; i44 < i5; i44++) {
                int i45 = i4 - 1;
                int i46 = ((i44 + i5) * i4) + i45;
                int i47 = (((i5 * 3) + i44) * i4) + i45;
                double d17 = (dArr[i46] + dArr[i47]) * -0.7071067811865475d;
                double d18 = (dArr[i46] - dArr[i47]) * 0.7071067811865475d;
                int i48 = i44 * 4;
                int i49 = (i44 * i4) + i45;
                dArr2[(i48 * i4) + i45] = dArr[i49] + d18;
                dArr2[((i48 + 2) * i4) + i45] = dArr[i49] - d18;
                int i50 = i45 + (((i5 * 2) + i44) * i4);
                dArr2[(i48 + 1) * i4] = d17 - dArr[i50];
                dArr2[(i48 + 3) * i4] = d17 + dArr[i50];
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void radb4(int i, int i2, double[] dArr, double[] dArr2, double[] dArr3, int i3) {
        int i4 = i;
        int i5 = i2;
        int i6 = i3 + i4;
        int i7 = i6 + i4;
        for (int i8 = 0; i8 < i5; i8++) {
            int i9 = i8 * 4;
            int i10 = i9 * i4;
            int i11 = i4 - 1;
            int i12 = ((i9 + 3) * i4) + i11;
            double d = dArr[i10] - dArr[i12];
            double d2 = dArr[i10] + dArr[i12];
            int i13 = i11 + ((i9 + 1) * i4);
            double d3 = dArr[i13] + dArr[i13];
            int i14 = (i9 + 2) * i4;
            double d4 = dArr[i14] + dArr[i14];
            dArr2[i8 * i4] = d2 + d3;
            dArr2[(i8 + i5) * i4] = d - d4;
            dArr2[((i5 * 2) + i8) * i4] = d2 - d3;
            dArr2[((i5 * 3) + i8) * i4] = d + d4;
        }
        if (i4 >= 2) {
            if (i4 != 2) {
                for (int i15 = 0; i15 < i5; i15++) {
                    for (int i16 = 2; i16 < i4; i16 += 2) {
                        int i17 = i4 - i16;
                        int i18 = i15 * 4;
                        int i19 = i18 * i4;
                        int i20 = i16 + i19;
                        int i21 = (i18 + 3) * i4;
                        int i22 = i17 + i21;
                        double d5 = dArr[i20] + dArr[i22];
                        double d6 = dArr[i20] - dArr[i22];
                        int i23 = (i18 + 2) * i4;
                        int i24 = i16 + i23;
                        int i25 = (i18 + 1) * i4;
                        int i26 = i17 + i25;
                        double d7 = dArr[i24] - dArr[i26];
                        double d8 = dArr[i24] + dArr[i26];
                        int i27 = i16 - 1;
                        int i28 = i27 + i19;
                        int i29 = i17 - 1;
                        int i30 = i29 + i21;
                        double d9 = dArr[i28] - dArr[i30];
                        double d10 = dArr[i28] + dArr[i30];
                        int i31 = i27 + i23;
                        int i32 = i29 + i25;
                        double d11 = dArr[i31] - dArr[i32];
                        double d12 = dArr[i31] + dArr[i32];
                        int i33 = i15 * i4;
                        dArr2[i27 + i33] = d10 + d12;
                        double d13 = d10 - d12;
                        dArr2[i33 + i16] = d6 + d7;
                        double d14 = d6 - d7;
                        double d15 = d9 - d8;
                        double d16 = d9 + d8;
                        double d17 = d5 + d11;
                        double d18 = d5 - d11;
                        int i34 = (i15 + i5) * i4;
                        int i35 = i16 - 2;
                        int i36 = i35 + i3;
                        int i37 = i27 + i3;
                        dArr2[i27 + i34] = (dArr3[i36] * d15) - (dArr3[i37] * d17);
                        dArr2[i16 + i34] = (dArr3[i36] * d17) + (dArr3[i37] * d15);
                        int i38 = ((i5 * 2) + i15) * i4;
                        int i39 = i35 + i6;
                        int i40 = i27 + i6;
                        dArr2[i27 + i38] = (dArr3[i39] * d13) - (dArr3[i40] * d14);
                        dArr2[i38 + i16] = (dArr3[i39] * d14) + (dArr3[i40] * d13);
                        int i41 = ((i5 * 3) + i15) * i4;
                        int i42 = i27 + i41;
                        int i43 = i35 + i7;
                        int i44 = i27 + i7;
                        dArr2[i42] = (dArr3[i43] * d16) - (dArr3[i44] * d18);
                        dArr2[i41 + i16] = (dArr3[i43] * d18) + (dArr3[i44] * d16);
                    }
                }
                if (i4 % 2 == 1) {
                    return;
                }
            }
            for (int i45 = 0; i45 < i5; i45++) {
                int i46 = i45 * 4;
                int i47 = (i46 + 1) * i4;
                int i48 = (i46 + 3) * i4;
                double d19 = dArr[i47] + dArr[i48];
                double d20 = dArr[i48] - dArr[i47];
                int i49 = i4 - 1;
                int i50 = (i46 * i4) + i49;
                int i51 = ((i46 + 2) * i4) + i49;
                double d21 = dArr[i50] - dArr[i51];
                double d22 = dArr[i50] + dArr[i51];
                dArr2[(i45 * i4) + i49] = d22 + d22;
                dArr2[((i45 + i5) * i4) + i49] = (d21 - d19) * 1.414213562373095d;
                dArr2[(((i5 * 2) + i45) * i4) + i49] = d20 + d20;
                dArr2[i49 + (((i5 * 3) + i45) * i4)] = (d21 + d19) * -1.414213562373095d;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void radf5(int i, int i2, double[] dArr, double[] dArr2, double[] dArr3, int i3) {
        int i4 = i;
        int i5 = i2;
        int i6 = i3 + i4;
        int i7 = i6 + i4;
        int i8 = i7 + i4;
        for (int i9 = 0; i9 < i5; i9++) {
            int i10 = ((i5 * 4) + i9) * i4;
            int i11 = (i9 + i5) * i4;
            double d = dArr[i10] + dArr[i11];
            double d2 = dArr[i10] - dArr[i11];
            int i12 = ((i5 * 3) + i9) * i4;
            int i13 = (i9 + (i5 * 2)) * i4;
            double d3 = dArr[i12] + dArr[i13];
            double d4 = dArr[i12] - dArr[i13];
            int i14 = i9 * 5;
            int i15 = i9 * i4;
            dArr2[i14 * i4] = dArr[i15] + d + d3;
            int i16 = i4 - 1;
            dArr2[i16 + ((i14 + 1) * i4)] = dArr[i15] + (d * 0.309016994374947d) + (d3 * -0.809016994374947d);
            dArr2[(i14 + 2) * i4] = (d2 * 0.951056516295154d) + (d4 * 0.587785252292473d);
            dArr2[i16 + ((i14 + 3) * i4)] = dArr[i15] + (d * -0.809016994374947d) + (d3 * 0.309016994374947d);
            dArr2[(i14 + 4) * i4] = (d2 * 0.587785252292473d) - (d4 * 0.951056516295154d);
        }
        if (i4 != 1) {
            for (int i17 = 0; i17 < i5; i17++) {
                for (int i18 = 2; i18 < i4; i18 += 2) {
                    int i19 = i4 - i18;
                    int i20 = i18 - 2;
                    int i21 = i20 + i3;
                    int i22 = i18 - 1;
                    int i23 = (i17 + i5) * i4;
                    int i24 = i22 + i23;
                    int i25 = i22 + i3;
                    int i26 = i18 + i23;
                    double d5 = (dArr3[i21] * dArr[i24]) + (dArr3[i25] * dArr[i26]);
                    double d6 = (dArr3[i21] * dArr[i26]) - (dArr3[i25] * dArr[i24]);
                    int i27 = i20 + i6;
                    int i28 = (i17 + (i5 * 2)) * i4;
                    int i29 = i22 + i28;
                    int i30 = i22 + i6;
                    int i31 = i18 + i28;
                    double d7 = (dArr3[i27] * dArr[i29]) + (dArr3[i30] * dArr[i31]);
                    double d8 = (dArr3[i27] * dArr[i31]) - (dArr3[i30] * dArr[i29]);
                    int i32 = i20 + i7;
                    int i33 = (i17 + (i5 * 3)) * i4;
                    int i34 = i22 + i33;
                    int i35 = i22 + i7;
                    int i36 = i18 + i33;
                    double d9 = (dArr3[i32] * dArr[i34]) + (dArr3[i35] * dArr[i36]);
                    double d10 = (dArr3[i32] * dArr[i36]) - (dArr3[i35] * dArr[i34]);
                    int i37 = i20 + i8;
                    int i38 = (i17 + (i5 * 4)) * i4;
                    int i39 = i22 + i38;
                    int i40 = i22 + i8;
                    int i41 = i18 + i38;
                    double d11 = (dArr3[i37] * dArr[i39]) + (dArr3[i40] * dArr[i41]);
                    double d12 = (dArr3[i37] * dArr[i41]) - (dArr3[i40] * dArr[i39]);
                    double d13 = d5 + d11;
                    double d14 = d11 - d5;
                    double d15 = d6 - d12;
                    double d16 = d6 + d12;
                    double d17 = d7 + d9;
                    double d18 = d9 - d7;
                    double d19 = d8 - d10;
                    double d20 = d8 + d10;
                    int i42 = i17 * 5;
                    int i43 = i42 * i4;
                    int i44 = i17 * i4;
                    int i45 = i22 + i44;
                    dArr2[i22 + i43] = dArr[i45] + d13 + d17;
                    int i46 = i18 + i44;
                    dArr2[i18 + i43] = dArr[i46] + d16 + d20;
                    double d21 = dArr[i45] + (d13 * 0.309016994374947d) + (d17 * -0.809016994374947d);
                    double d22 = dArr[i46] + (d16 * 0.309016994374947d) + (d20 * -0.809016994374947d);
                    double d23 = dArr[i45] + (d13 * -0.809016994374947d) + (d17 * 0.309016994374947d);
                    double d24 = dArr[i46] + (d16 * -0.809016994374947d) + (d20 * 0.309016994374947d);
                    double d25 = (d15 * 0.951056516295154d) + (d19 * 0.587785252292473d);
                    double d26 = (d14 * 0.951056516295154d) + (d18 * 0.587785252292473d);
                    double d27 = (d15 * 0.587785252292473d) - (d19 * 0.951056516295154d);
                    double d28 = (d14 * 0.587785252292473d) - (d18 * 0.951056516295154d);
                    int i47 = (i42 + 2) * i4;
                    dArr2[i22 + i47] = d21 + d25;
                    int i48 = i19 - 1;
                    int i49 = (i42 + 1) * i4;
                    dArr2[i48 + i49] = d21 - d25;
                    dArr2[i18 + i47] = d22 + d26;
                    dArr2[i19 + i49] = d26 - d22;
                    int i50 = (i42 + 4) * i4;
                    dArr2[i22 + i50] = d23 + d27;
                    int i51 = (i42 + 3) * i4;
                    dArr2[i48 + i51] = d23 - d27;
                    dArr2[i18 + i50] = d24 + d28;
                    dArr2[i19 + i51] = d28 - d24;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void radb5(int i, int i2, double[] dArr, double[] dArr2, double[] dArr3, int i3) {
        int i4 = i;
        int i5 = i2;
        int i6 = i3 + i4;
        int i7 = i6 + i4;
        int i8 = i7 + i4;
        for (int i9 = 0; i9 < i5; i9++) {
            int i10 = i9 * 5;
            double d = dArr[(i10 + 2) * i4] * 2.0d;
            double d2 = dArr[(i10 + 4) * i4] * 2.0d;
            int i11 = i4 - 1;
            double d3 = dArr[i11 + ((i10 + 1) * i4)] * 2.0d;
            double d4 = dArr[i11 + ((i10 + 3) * i4)] * 2.0d;
            int i12 = i10 * i4;
            dArr2[i9 * i4] = dArr[i12] + d3 + d4;
            double d5 = dArr[i12] + (d3 * 0.309016994374947d) + (d4 * -0.809016994374947d);
            double d6 = dArr[i12] + (d3 * -0.809016994374947d) + (d4 * 0.309016994374947d);
            double d7 = (d * 0.951056516295154d) + (d2 * 0.587785252292473d);
            double d8 = (d * 0.587785252292473d) - (d2 * 0.951056516295154d);
            dArr2[(i9 + i5) * i4] = d5 - d7;
            dArr2[((i5 * 2) + i9) * i4] = d6 - d8;
            dArr2[((i5 * 3) + i9) * i4] = d6 + d8;
            dArr2[((i5 * 4) + i9) * i4] = d5 + d7;
        }
        if (i4 != 1) {
            for (int i13 = 0; i13 < i5; i13++) {
                for (int i14 = 2; i14 < i4; i14 += 2) {
                    int i15 = i4 - i14;
                    int i16 = i13 * 5;
                    int i17 = (i16 + 2) * i4;
                    int i18 = i14 + i17;
                    int i19 = (i16 + 1) * i4;
                    int i20 = i15 + i19;
                    double d9 = dArr[i18] + dArr[i20];
                    double d10 = dArr[i18] - dArr[i20];
                    int i21 = (i16 + 4) * i4;
                    int i22 = i14 + i21;
                    int i23 = (i16 + 3) * i4;
                    int i24 = i15 + i23;
                    double d11 = dArr[i22] + dArr[i24];
                    double d12 = dArr[i22] - dArr[i24];
                    int i25 = i14 - 1;
                    int i26 = i25 + i17;
                    int i27 = i15 - 1;
                    int i28 = i27 + i19;
                    double d13 = dArr[i26] - dArr[i28];
                    double d14 = dArr[i26] + dArr[i28];
                    int i29 = i25 + i21;
                    int i30 = i27 + i23;
                    double d15 = dArr[i29] - dArr[i30];
                    double d16 = dArr[i29] + dArr[i30];
                    int i31 = i13 * i4;
                    int i32 = i16 * i4;
                    int i33 = i25 + i32;
                    dArr2[i25 + i31] = dArr[i33] + d14 + d16;
                    int i34 = i32 + i14;
                    dArr2[i31 + i14] = dArr[i34] + d10 + d12;
                    double d17 = dArr[i33] + (d14 * 0.309016994374947d) + (d16 * -0.809016994374947d);
                    double d18 = dArr[i34] + (d10 * 0.309016994374947d) + (d12 * -0.809016994374947d);
                    double d19 = dArr[i33] + (d14 * -0.809016994374947d) + (d16 * 0.309016994374947d);
                    double d20 = dArr[i34] + (d10 * -0.809016994374947d) + (d12 * 0.309016994374947d);
                    double d21 = (d13 * 0.951056516295154d) + (d15 * 0.587785252292473d);
                    double d22 = (d9 * 0.951056516295154d) + (d11 * 0.587785252292473d);
                    double d23 = (d13 * 0.587785252292473d) - (d15 * 0.951056516295154d);
                    double d24 = (d9 * 0.587785252292473d) - (d11 * 0.951056516295154d);
                    double d25 = d19 - d24;
                    double d26 = d19 + d24;
                    double d27 = d20 + d23;
                    double d28 = d20 - d23;
                    double d29 = d17 + d22;
                    double d30 = d17 - d22;
                    double d31 = d18 - d21;
                    double d32 = d18 + d21;
                    int i35 = (i13 + i5) * i4;
                    int i36 = i14 - 2;
                    int i37 = i36 + i3;
                    int i38 = i25 + i3;
                    dArr2[i25 + i35] = (dArr3[i37] * d30) - (dArr3[i38] * d32);
                    dArr2[i35 + i14] = (dArr3[i37] * d32) + (dArr3[i38] * d30);
                    int i39 = ((i5 * 2) + i13) * i4;
                    int i40 = i36 + i6;
                    int i41 = i25 + i6;
                    dArr2[i25 + i39] = (dArr3[i40] * d25) - (dArr3[i41] * d27);
                    dArr2[i39 + i14] = (dArr3[i40] * d27) + (dArr3[i41] * d25);
                    int i42 = ((i5 * 3) + i13) * i4;
                    int i43 = i36 + i7;
                    int i44 = i25 + i7;
                    dArr2[i25 + i42] = (dArr3[i43] * d26) - (dArr3[i44] * d28);
                    dArr2[i42 + i14] = (dArr3[i43] * d28) + (dArr3[i44] * d26);
                    int i45 = ((i5 * 4) + i13) * i4;
                    int i46 = i25 + i45;
                    int i47 = i36 + i8;
                    int i48 = i25 + i8;
                    dArr2[i46] = (dArr3[i47] * d29) - (dArr3[i48] * d31);
                    dArr2[i45 + i14] = (dArr3[i47] * d31) + (dArr3[i48] * d29);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void radfg(int i, int i2, int i3, int i4, double[] dArr, double[] dArr2, double[] dArr3, double[] dArr4, double[] dArr5, double[] dArr6, int i5) {
        double d;
        int i6;
        int i7 = i;
        int i8 = i2;
        int i9 = i3;
        int i10 = i4;
        double d2 = (double) i8;
        Double.isNaN(d2);
        double d3 = 6.283185307179586d / d2;
        double cos = Math.cos(d3);
        int i11 = (i8 + 1) / 2;
        int i12 = i7 - 1;
        int i13 = i12 / 2;
        double sin = Math.sin(d3);
        if (i7 != 1) {
            for (int i14 = 0; i14 < i10; i14++) {
                dArr5[i14] = dArr3[i14];
            }
            for (int i15 = 1; i15 < i8; i15++) {
                for (int i16 = 0; i16 < i9; i16++) {
                    int i17 = (i16 + (i15 * i9)) * i7;
                    dArr4[i17] = dArr2[i17];
                }
            }
            if (i13 <= i9) {
                int i18 = -i7;
                int i19 = 1;
                while (i19 < i8) {
                    int i20 = i18 + i7;
                    int i21 = i20 - 1;
                    int i22 = i20;
                    int i23 = 2;
                    while (i23 < i7) {
                        i21 += 2;
                        double d4 = cos;
                        for (int i24 = 0; i24 < i9; i24++) {
                            int i25 = (i24 + (i19 * i9)) * i7;
                            int i26 = (i23 - 1) + i25;
                            int i27 = (i21 - 1) + i5;
                            int i28 = i21 + i5;
                            int i29 = i23 + i25;
                            dArr4[i26] = (dArr6[i27] * dArr2[i26]) + (dArr6[i28] * dArr2[i29]);
                            dArr4[i29] = (dArr6[i27] * dArr2[i29]) - (dArr6[i28] * dArr2[i26]);
                        }
                        i23 += 2;
                        cos = d4;
                    }
                    i19++;
                    i18 = i22;
                }
                d = cos;
            } else {
                d = cos;
                int i30 = -i7;
                for (int i31 = 1; i31 < i8; i31++) {
                    i30 += i7;
                    for (int i32 = 0; i32 < i9; i32++) {
                        int i33 = i30 - 1;
                        for (int i34 = 2; i34 < i7; i34 += 2) {
                            i33 += 2;
                            int i35 = (i32 + (i31 * i9)) * i7;
                            int i36 = (i34 - 1) + i35;
                            int i37 = (i33 - 1) + i5;
                            int i38 = i33 + i5;
                            int i39 = i34 + i35;
                            dArr4[i36] = (dArr6[i37] * dArr2[i36]) + (dArr6[i38] * dArr2[i39]);
                            dArr4[i39] = (dArr6[i37] * dArr2[i39]) - (dArr6[i38] * dArr2[i36]);
                        }
                    }
                }
            }
            if (i13 >= i9) {
                for (int i40 = 1; i40 < i11; i40++) {
                    int i41 = i8 - i40;
                    for (int i42 = 0; i42 < i9; i42++) {
                        for (int i43 = 2; i43 < i7; i43 += 2) {
                            int i44 = i43 - 1;
                            int i45 = ((i40 * i9) + i42) * i7;
                            int i46 = i44 + i45;
                            int i47 = (i42 + (i41 * i9)) * i7;
                            int i48 = i44 + i47;
                            dArr2[i46] = dArr4[i46] + dArr4[i48];
                            int i49 = i45 + i43;
                            int i50 = i43 + i47;
                            dArr2[i48] = dArr4[i49] - dArr4[i50];
                            dArr2[i49] = dArr4[i49] + dArr4[i50];
                            dArr2[i50] = dArr4[i48] - dArr4[i46];
                        }
                    }
                }
            } else {
                for (int i51 = 1; i51 < i11; i51++) {
                    int i52 = i8 - i51;
                    for (int i53 = 2; i53 < i7; i53 += 2) {
                        for (int i54 = 0; i54 < i9; i54++) {
                            int i55 = i53 - 1;
                            int i56 = ((i51 * i9) + i54) * i7;
                            int i57 = i55 + i56;
                            int i58 = (i54 + (i52 * i9)) * i7;
                            int i59 = i55 + i58;
                            dArr2[i57] = dArr4[i57] + dArr4[i59];
                            int i60 = i56 + i53;
                            int i61 = i53 + i58;
                            dArr2[i59] = dArr4[i60] - dArr4[i61];
                            dArr2[i60] = dArr4[i60] + dArr4[i61];
                            dArr2[i61] = dArr4[i59] - dArr4[i57];
                        }
                    }
                }
            }
        } else {
            d = cos;
            for (int i62 = 0; i62 < i10; i62++) {
                dArr3[i62] = dArr5[i62];
            }
        }
        for (int i63 = 1; i63 < i11; i63++) {
            int i64 = i8 - i63;
            for (int i65 = 0; i65 < i9; i65++) {
                int i66 = ((i63 * i9) + i65) * i7;
                int i67 = (i65 + (i64 * i9)) * i7;
                dArr2[i66] = dArr4[i66] + dArr4[i67];
                dArr2[i67] = dArr4[i67] - dArr4[i66];
            }
        }
        double d5 = 1.0d;
        double d6 = 0.0d;
        int i68 = 1;
        while (i68 < i11) {
            int i69 = i8 - i68;
            double d7 = (d * d5) - (sin * d6);
            d6 = (d6 * d) + (d5 * sin);
            for (int i70 = 0; i70 < i10; i70++) {
                dArr5[(i68 * i10) + i70] = dArr3[i70] + (dArr3[i70 + i10] * d7);
                dArr5[(i69 * i10) + i70] = dArr3[((i8 - 1) * i10) + i70] * d6;
            }
            double d8 = d6;
            double d9 = d7;
            int i71 = 2;
            while (i71 < i11) {
                int i72 = i8 - i71;
                double d10 = (d7 * d9) - (d6 * d8);
                d8 = (d8 * d7) + (d9 * d6);
                for (int i73 = 0; i73 < i10; i73++) {
                    int i74 = i73 + (i68 * i10);
                    dArr5[i74] = dArr5[i74] + (dArr3[i73 + (i71 * i10)] * d10);
                    int i75 = i73 + (i69 * i10);
                    dArr5[i75] = dArr5[i75] + (dArr3[i73 + (i72 * i10)] * d8);
                }
                i71++;
                d9 = d10;
            }
            i68++;
            d5 = d7;
        }
        for (int i76 = 1; i76 < i11; i76++) {
            for (int i77 = 0; i77 < i10; i77++) {
                dArr5[i77] = dArr5[i77] + dArr3[(i76 * i10) + i77];
            }
        }
        if (i7 >= i9) {
            for (int i78 = 0; i78 < i9; i78++) {
                for (int i79 = 0; i79 < i7; i79++) {
                    dArr[(i78 * i8 * i7) + i79] = dArr4[(i78 * i7) + i79];
                }
            }
            i6 = i13;
        } else {
            i6 = i13;
            for (int i80 = 0; i80 < i7; i80++) {
                for (int i81 = 0; i81 < i9; i81++) {
                    dArr[(i81 * i8 * i7) + i80] = dArr4[(i81 * i7) + i80];
                }
            }
        }
        for (int i82 = 1; i82 < i11; i82++) {
            int i83 = i8 - i82;
            int i84 = i82 * 2;
            for (int i85 = 0; i85 < i9; i85++) {
                int i86 = i85 * i8;
                dArr[i12 + (((i84 - 1) + i86) * i7)] = dArr4[((i82 * i9) + i85) * i7];
                dArr[(i86 + i84) * i7] = dArr4[((i83 * i9) + i85) * i7];
            }
        }
        if (i7 != 1) {
            if (i6 >= i9) {
                for (int i87 = 1; i87 < i11; i87++) {
                    int i88 = i8 - i87;
                    int i89 = i87 * 2;
                    for (int i90 = 0; i90 < i9; i90++) {
                        for (int i91 = 2; i91 < i7; i91 += 2) {
                            int i92 = i7 - i91;
                            int i93 = i91 - 1;
                            int i94 = i90 * i8;
                            int i95 = (i89 + i94) * i7;
                            int i96 = i93 + i95;
                            int i97 = (i90 + (i87 * i9)) * i7;
                            int i98 = i93 + i97;
                            int i99 = (i90 + (i88 * i9)) * i7;
                            int i100 = i93 + i99;
                            dArr[i96] = dArr4[i98] + dArr4[i100];
                            int i101 = ((i89 - 1) + i94) * i7;
                            dArr[(i92 - 1) + i101] = dArr4[i98] - dArr4[i100];
                            int i102 = i91 + i97;
                            int i103 = i91 + i99;
                            dArr[i95 + i91] = dArr4[i102] + dArr4[i103];
                            dArr[i92 + i101] = dArr4[i103] - dArr4[i102];
                        }
                    }
                }
                return;
            }
            for (int i104 = 1; i104 < i11; i104++) {
                int i105 = i8 - i104;
                int i106 = i104 * 2;
                for (int i107 = 2; i107 < i7; i107 += 2) {
                    int i108 = i7 - i107;
                    for (int i109 = 0; i109 < i9; i109++) {
                        int i110 = i107 - 1;
                        int i111 = i109 * i8;
                        int i112 = (i106 + i111) * i7;
                        int i113 = i110 + i112;
                        int i114 = (i109 + (i104 * i9)) * i7;
                        int i115 = i110 + i114;
                        int i116 = (i109 + (i105 * i9)) * i7;
                        int i117 = i110 + i116;
                        dArr[i113] = dArr4[i115] + dArr4[i117];
                        int i118 = ((i106 - 1) + i111) * i7;
                        dArr[(i108 - 1) + i118] = dArr4[i115] - dArr4[i117];
                        int i119 = i107 + i114;
                        int i120 = i107 + i116;
                        dArr[i112 + i107] = dArr4[i119] + dArr4[i120];
                        dArr[i108 + i118] = dArr4[i120] - dArr4[i119];
                    }
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0178  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x01f9  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x020f  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0235 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0236  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void radbg(int r35, int r36, int r37, int r38, double[] r39, double[] r40, double[] r41, double[] r42, double[] r43, double[] r44, int r45) {
        /*
            r34 = this;
            r0 = r35
            r1 = r36
            r2 = r37
            r3 = r38
            double r10 = (double) r1
            r12 = 4618760256179416344(0x401921fb54442d18, double:6.283185307179586)
            java.lang.Double.isNaN(r10)
            double r12 = r12 / r10
            double r10 = java.lang.Math.cos(r12)
            double r12 = java.lang.Math.sin(r12)
            int r14 = r0 + -1
            int r8 = r14 / 2
            int r15 = r1 + 1
            r16 = 2
            int r9 = r15 / 2
            r15 = 0
            if (r0 < r2) goto L_0x0041
            r5 = 0
        L_0x0028:
            if (r5 >= r2) goto L_0x005b
            r6 = 0
        L_0x002b:
            if (r6 >= r0) goto L_0x003e
            int r17 = r5 * r0
            int r17 = r6 + r17
            int r18 = r5 * r1
            int r18 = r18 * r0
            int r18 = r6 + r18
            r18 = r39[r18]
            r42[r17] = r18
            int r6 = r6 + 1
            goto L_0x002b
        L_0x003e:
            int r5 = r5 + 1
            goto L_0x0028
        L_0x0041:
            r5 = 0
        L_0x0042:
            if (r5 >= r0) goto L_0x005b
            r6 = 0
        L_0x0045:
            if (r6 >= r2) goto L_0x0058
            int r17 = r6 * r0
            int r17 = r5 + r17
            int r18 = r6 * r1
            int r18 = r18 * r0
            int r18 = r5 + r18
            r18 = r39[r18]
            r42[r17] = r18
            int r6 = r6 + 1
            goto L_0x0045
        L_0x0058:
            int r5 = r5 + 1
            goto L_0x0042
        L_0x005b:
            r6 = 1
        L_0x005c:
            if (r6 >= r9) goto L_0x0095
            int r17 = r1 - r6
            int r18 = r6 * 2
            r5 = 0
        L_0x0063:
            if (r5 >= r2) goto L_0x0092
            int r19 = r6 * r2
            int r19 = r5 + r19
            int r19 = r19 * r0
            int r21 = r18 + -1
            int r22 = r5 * r1
            int r21 = r21 + r22
            int r21 = r21 * r0
            int r21 = r14 + r21
            r23 = r39[r21]
            r25 = r39[r21]
            double r23 = r23 + r25
            r42[r19] = r23
            int r19 = r17 * r2
            int r19 = r5 + r19
            int r19 = r19 * r0
            int r22 = r18 + r22
            int r22 = r22 * r0
            r23 = r39[r22]
            r21 = r39[r22]
            double r23 = r23 + r21
            r42[r19] = r23
            int r5 = r5 + 1
            goto L_0x0063
        L_0x0092:
            int r6 = r6 + 1
            goto L_0x005c
        L_0x0095:
            r5 = 1
            if (r0 == r5) goto L_0x016e
            if (r8 < r2) goto L_0x0106
            r5 = 1
        L_0x009b:
            if (r5 >= r9) goto L_0x016e
            int r6 = r1 - r5
            r14 = 0
        L_0x00a0:
            if (r14 >= r2) goto L_0x0101
            r27 = r8
            r8 = 2
        L_0x00a5:
            if (r8 >= r0) goto L_0x00fc
            int r17 = r0 - r8
            int r18 = r8 + -1
            int r19 = r5 * r2
            int r19 = r14 + r19
            int r19 = r19 * r0
            int r21 = r18 + r19
            int r22 = r5 * 2
            int r23 = r14 * r1
            int r24 = r22 + r23
            int r24 = r24 * r0
            int r25 = r18 + r24
            r28 = r39[r25]
            int r26 = r17 + -1
            r20 = 1
            int r22 = r22 + -1
            int r22 = r22 + r23
            int r22 = r22 * r0
            int r26 = r26 + r22
            r30 = r39[r26]
            double r28 = r28 + r30
            r42[r21] = r28
            int r21 = r6 * r2
            int r21 = r14 + r21
            int r21 = r21 * r0
            int r18 = r18 + r21
            r28 = r39[r25]
            r25 = r39[r26]
            double r28 = r28 - r25
            r42[r18] = r28
            int r19 = r8 + r19
            int r24 = r8 + r24
            r25 = r39[r24]
            int r17 = r17 + r22
            r22 = r39[r17]
            double r25 = r25 - r22
            r42[r19] = r25
            int r21 = r8 + r21
            r18 = r39[r24]
            r22 = r39[r17]
            double r18 = r18 + r22
            r42[r21] = r18
            int r8 = r8 + 2
            goto L_0x00a5
        L_0x00fc:
            int r14 = r14 + 1
            r8 = r27
            goto L_0x00a0
        L_0x0101:
            r27 = r8
            int r5 = r5 + 1
            goto L_0x009b
        L_0x0106:
            r27 = r8
            r5 = 1
        L_0x0109:
            if (r5 >= r9) goto L_0x0170
            int r6 = r1 - r5
            r8 = 2
        L_0x010e:
            if (r8 >= r0) goto L_0x016b
            int r14 = r0 - r8
            r7 = 0
        L_0x0113:
            if (r7 >= r2) goto L_0x0168
            int r17 = r8 + -1
            int r18 = r5 * r2
            int r18 = r7 + r18
            int r18 = r18 * r0
            int r19 = r17 + r18
            int r21 = r5 * 2
            int r22 = r7 * r1
            int r23 = r21 + r22
            int r23 = r23 * r0
            int r24 = r17 + r23
            r25 = r39[r24]
            int r28 = r14 + -1
            r20 = 1
            int r21 = r21 + -1
            int r21 = r21 + r22
            int r21 = r21 * r0
            int r28 = r28 + r21
            r29 = r39[r28]
            double r25 = r25 + r29
            r42[r19] = r25
            int r19 = r6 * r2
            int r19 = r7 + r19
            int r19 = r19 * r0
            int r17 = r17 + r19
            r24 = r39[r24]
            r28 = r39[r28]
            double r24 = r24 - r28
            r42[r17] = r24
            int r18 = r8 + r18
            int r23 = r8 + r23
            r24 = r39[r23]
            int r21 = r14 + r21
            r28 = r39[r21]
            double r24 = r24 - r28
            r42[r18] = r24
            int r19 = r8 + r19
            r17 = r39[r23]
            r21 = r39[r21]
            double r17 = r17 + r21
            r42[r19] = r17
            int r7 = r7 + 1
            goto L_0x0113
        L_0x0168:
            int r8 = r8 + 2
            goto L_0x010e
        L_0x016b:
            int r5 = r5 + 1
            goto L_0x0109
        L_0x016e:
            r27 = r8
        L_0x0170:
            r4 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            r7 = 0
            r5 = r4
            r4 = 1
        L_0x0176:
            if (r4 >= r9) goto L_0x01f6
            int r14 = r1 - r4
            double r17 = r10 * r5
            double r21 = r12 * r7
            double r17 = r17 - r21
            double r7 = r7 * r10
            double r5 = r5 * r12
            double r7 = r7 + r5
            r5 = 0
        L_0x0186:
            if (r5 >= r3) goto L_0x01a9
            int r6 = r4 * r3
            int r6 = r6 + r5
            r21 = r43[r5]
            int r23 = r5 + r3
            r23 = r43[r23]
            double r23 = r23 * r17
            double r21 = r21 + r23
            r41[r6] = r21
            int r6 = r14 * r3
            int r6 = r6 + r5
            int r21 = r1 + -1
            int r21 = r21 * r3
            int r21 = r5 + r21
            r21 = r43[r21]
            double r21 = r21 * r7
            r41[r6] = r21
            int r5 = r5 + 1
            goto L_0x0186
        L_0x01a9:
            r24 = r7
            r21 = r17
            r5 = 2
        L_0x01ae:
            if (r5 >= r9) goto L_0x01ef
            int r6 = r1 - r5
            double r28 = r17 * r21
            double r30 = r7 * r24
            double r28 = r28 - r30
            double r24 = r24 * r17
            double r21 = r21 * r7
            double r24 = r24 + r21
            r32 = r7
            r7 = 0
        L_0x01c1:
            if (r7 >= r3) goto L_0x01e8
            int r8 = r4 * r3
            int r8 = r8 + r7
            r21 = r41[r8]
            int r26 = r5 * r3
            int r26 = r7 + r26
            r30 = r43[r26]
            double r30 = r30 * r28
            double r21 = r21 + r30
            r41[r8] = r21
            int r8 = r14 * r3
            int r8 = r8 + r7
            r21 = r41[r8]
            int r26 = r6 * r3
            int r26 = r7 + r26
            r30 = r43[r26]
            double r30 = r30 * r24
            double r21 = r21 + r30
            r41[r8] = r21
            int r7 = r7 + 1
            goto L_0x01c1
        L_0x01e8:
            int r5 = r5 + 1
            r21 = r28
            r7 = r32
            goto L_0x01ae
        L_0x01ef:
            r32 = r7
            int r4 = r4 + 1
            r5 = r17
            goto L_0x0176
        L_0x01f6:
            r4 = 1
        L_0x01f7:
            if (r4 >= r9) goto L_0x020c
            r5 = 0
        L_0x01fa:
            if (r5 >= r3) goto L_0x0209
            r7 = r43[r5]
            int r6 = r4 * r3
            int r6 = r6 + r5
            r10 = r43[r6]
            double r7 = r7 + r10
            r43[r5] = r7
            int r5 = r5 + 1
            goto L_0x01fa
        L_0x0209:
            int r4 = r4 + 1
            goto L_0x01f7
        L_0x020c:
            r4 = 1
        L_0x020d:
            if (r4 >= r9) goto L_0x0232
            int r5 = r1 - r4
            r6 = 0
        L_0x0212:
            if (r6 >= r2) goto L_0x022f
            int r7 = r4 * r2
            int r7 = r7 + r6
            int r7 = r7 * r0
            r10 = r40[r7]
            int r12 = r5 * r2
            int r12 = r12 + r6
            int r12 = r12 * r0
            r13 = r40[r12]
            double r10 = r10 - r13
            r42[r7] = r10
            r10 = r40[r7]
            r13 = r40[r12]
            double r10 = r10 + r13
            r42[r12] = r10
            int r6 = r6 + 1
            goto L_0x0212
        L_0x022f:
            int r4 = r4 + 1
            goto L_0x020d
        L_0x0232:
            r5 = 1
            if (r0 != r5) goto L_0x0236
            return
        L_0x0236:
            r14 = r27
            if (r14 < r2) goto L_0x0282
            r4 = 1
        L_0x023b:
            if (r4 >= r9) goto L_0x02ca
            int r6 = r1 - r4
            r7 = 0
        L_0x0240:
            if (r7 >= r2) goto L_0x027f
            r10 = 2
        L_0x0243:
            if (r10 >= r0) goto L_0x027c
            int r11 = r10 + -1
            int r12 = r4 * r2
            int r12 = r12 + r7
            int r12 = r12 * r0
            int r13 = r11 + r12
            r17 = r40[r13]
            int r20 = r6 * r2
            int r20 = r7 + r20
            int r20 = r20 * r0
            int r21 = r10 + r20
            r24 = r40[r21]
            double r17 = r17 - r24
            r42[r13] = r17
            int r11 = r11 + r20
            r17 = r40[r13]
            r24 = r40[r21]
            double r17 = r17 + r24
            r42[r11] = r17
            int r12 = r12 + r10
            r17 = r40[r12]
            r24 = r40[r11]
            double r17 = r17 + r24
            r42[r12] = r17
            r12 = r40[r12]
            r17 = r40[r11]
            double r12 = r12 - r17
            r42[r21] = r12
            int r10 = r10 + 2
            goto L_0x0243
        L_0x027c:
            int r7 = r7 + 1
            goto L_0x0240
        L_0x027f:
            int r4 = r4 + 1
            goto L_0x023b
        L_0x0282:
            r4 = 1
        L_0x0283:
            if (r4 >= r9) goto L_0x02ca
            int r6 = r1 - r4
            r7 = 2
        L_0x0288:
            if (r7 >= r0) goto L_0x02c7
            r10 = 0
        L_0x028b:
            if (r10 >= r2) goto L_0x02c4
            int r11 = r7 + -1
            int r12 = r4 * r2
            int r12 = r12 + r10
            int r12 = r12 * r0
            int r13 = r11 + r12
            r17 = r40[r13]
            int r20 = r6 * r2
            int r20 = r10 + r20
            int r20 = r20 * r0
            int r21 = r7 + r20
            r24 = r40[r21]
            double r17 = r17 - r24
            r42[r13] = r17
            int r11 = r11 + r20
            r17 = r40[r13]
            r24 = r40[r21]
            double r17 = r17 + r24
            r42[r11] = r17
            int r12 = r12 + r7
            r17 = r40[r12]
            r24 = r40[r11]
            double r17 = r17 + r24
            r42[r12] = r17
            r12 = r40[r12]
            r17 = r40[r11]
            double r12 = r12 - r17
            r42[r21] = r12
            int r10 = r10 + 1
            goto L_0x028b
        L_0x02c4:
            int r7 = r7 + 2
            goto L_0x0288
        L_0x02c7:
            int r4 = r4 + 1
            goto L_0x0283
        L_0x02ca:
            r4 = 0
        L_0x02cb:
            if (r4 >= r3) goto L_0x02d4
            r9 = r43[r4]
            r41[r4] = r9
            int r4 = r4 + 1
            goto L_0x02cb
        L_0x02d4:
            r3 = 1
        L_0x02d5:
            if (r3 >= r1) goto L_0x02e9
            r4 = 0
        L_0x02d8:
            if (r4 >= r2) goto L_0x02e6
            int r6 = r3 * r2
            int r6 = r6 + r4
            int r6 = r6 * r0
            r9 = r42[r6]
            r40[r6] = r9
            int r4 = r4 + 1
            goto L_0x02d8
        L_0x02e6:
            int r3 = r3 + 1
            goto L_0x02d5
        L_0x02e9:
            if (r14 > r2) goto L_0x0332
            int r3 = -r0
        L_0x02ec:
            if (r5 >= r1) goto L_0x0379
            int r3 = r3 + r0
            int r4 = r3 + -1
            r6 = r4
            r4 = 2
        L_0x02f3:
            if (r4 >= r0) goto L_0x032f
            int r6 = r6 + 2
            r7 = 0
        L_0x02f8:
            if (r7 >= r2) goto L_0x032c
            int r9 = r4 + -1
            int r10 = r5 * r2
            int r10 = r10 + r7
            int r10 = r10 * r0
            int r9 = r9 + r10
            int r11 = r6 + -1
            int r11 = r11 + r45
            r17 = r44[r11]
            r19 = r42[r9]
            double r17 = r17 * r19
            int r14 = r6 + r45
            r19 = r44[r14]
            int r10 = r10 + r4
            r21 = r42[r10]
            double r19 = r19 * r21
            double r17 = r17 - r19
            r40[r9] = r17
            r17 = r44[r11]
            r19 = r42[r10]
            double r17 = r17 * r19
            r19 = r44[r14]
            r21 = r42[r9]
            double r19 = r19 * r21
            double r17 = r17 + r19
            r40[r10] = r17
            int r7 = r7 + 1
            goto L_0x02f8
        L_0x032c:
            int r4 = r4 + 2
            goto L_0x02f3
        L_0x032f:
            int r5 = r5 + 1
            goto L_0x02ec
        L_0x0332:
            int r3 = -r0
        L_0x0333:
            if (r5 >= r1) goto L_0x0379
            int r3 = r3 + r0
            r4 = 0
        L_0x0337:
            if (r4 >= r2) goto L_0x0376
            int r6 = r3 + -1
            r7 = r6
            r6 = 2
        L_0x033d:
            if (r6 >= r0) goto L_0x0373
            int r7 = r7 + 2
            int r9 = r6 + -1
            int r10 = r5 * r2
            int r10 = r10 + r4
            int r10 = r10 * r0
            int r9 = r9 + r10
            int r11 = r7 + -1
            int r11 = r11 + r45
            r17 = r44[r11]
            r19 = r42[r9]
            double r17 = r17 * r19
            int r14 = r7 + r45
            r19 = r44[r14]
            int r10 = r10 + r6
            r21 = r42[r10]
            double r19 = r19 * r21
            double r17 = r17 - r19
            r40[r9] = r17
            r17 = r44[r11]
            r19 = r42[r10]
            double r17 = r17 * r19
            r19 = r44[r14]
            r21 = r42[r9]
            double r19 = r19 * r21
            double r17 = r17 + r19
            r40[r10] = r17
            int r6 = r6 + 2
            goto L_0x033d
        L_0x0373:
            int r4 = r4 + 1
            goto L_0x0337
        L_0x0376:
            int r5 = r5 + 1
            goto L_0x0333
        L_0x0379:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p004ca.uol.aig.fftpack.RealDoubleFFT_Mixed.radbg(int, int, int, int, double[], double[], double[], double[], double[], double[], int):void");
    }

    /* access modifiers changed from: package-private */
    public void rfftf1(int i, double[] dArr, double[] dArr2, int i2) {
        int i3;
        int i4;
        int i5 = i;
        double[] dArr3 = dArr2;
        int i6 = i2;
        double[] dArr4 = new double[i5];
        System.arraycopy(dArr3, i6, dArr4, 0, i5);
        int i7 = i5 * 2;
        int i8 = (int) dArr3[i7 + 1 + i6];
        int i9 = 1;
        int i10 = (i5 - 1) + i5 + i6;
        int i11 = 1;
        int i12 = 1;
        int i13 = i5;
        while (i12 <= i8) {
            int i14 = (int) dArr3[(i8 - i12) + 2 + i7 + i6];
            int i15 = i13 / i14;
            int i16 = i5 / i13;
            int i17 = i16 * i15;
            int i18 = i10 - ((i14 - 1) * i16);
            int i19 = 1 - i11;
            if (i14 == 4) {
                if (i19 == 0) {
                    radf4(i16, i15, dArr, dArr4, dArr2, i18);
                } else {
                    radf4(i16, i15, dArr4, dArr, dArr2, i18);
                }
            } else if (i14 == 2) {
                if (i19 == 0) {
                    radf2(i16, i15, dArr, dArr4, dArr2, i18);
                } else {
                    radf2(i16, i15, dArr4, dArr, dArr2, i18);
                }
            } else if (i14 == 3) {
                if (i19 == 0) {
                    radf3(i16, i15, dArr, dArr4, dArr2, i18);
                } else {
                    radf3(i16, i15, dArr4, dArr, dArr2, i18);
                }
            } else if (i14 != 5) {
                if (i16 == i9) {
                    i19 = 1 - i19;
                }
                if (i19 == 0) {
                    i4 = i12;
                    i3 = i8;
                    radfg(i16, i14, i15, i17, dArr, dArr, dArr, dArr4, dArr4, dArr2, i18);
                    i11 = 1;
                } else {
                    i4 = i12;
                    i3 = i8;
                    radfg(i16, i14, i15, i17, dArr4, dArr4, dArr4, dArr, dArr, dArr2, i18);
                    i11 = 0;
                }
                i12 = i4 + 1;
                i13 = i15;
                i10 = i18;
                i8 = i3;
                i9 = 1;
                dArr3 = dArr2;
            } else if (i19 == 0) {
                radf5(i16, i15, dArr, dArr4, dArr2, i18);
            } else {
                radf5(i16, i15, dArr4, dArr, dArr2, i18);
            }
            i11 = i19;
            i4 = i12;
            i3 = i8;
            i12 = i4 + 1;
            i13 = i15;
            i10 = i18;
            i8 = i3;
            i9 = 1;
            dArr3 = dArr2;
        }
        if (i11 != 1) {
            for (int i20 = 0; i20 < i5; i20++) {
                dArr[i20] = dArr4[i20];
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void rfftb1(int i, double[] dArr, double[] dArr2, int i2) {
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8 = 0;
        int i9 = i;
        double[] dArr3 = dArr2;
        int i10 = i2;
        double[] dArr4 = new double[i9];
        System.arraycopy(dArr3, i10, dArr4, 0, i9);
        int i11 = i9 * 2;
        int i12 = (int) dArr3[i11 + 1 + i10];
        int i13 = i9 + i10;
        int i14 = 1;
        int i15 = 1;
        int i16 = 0;
        while (i14 <= i12) {
            int i17 = i14 + 1;
            int i18 = (int) dArr3[i17 + i11 + i10];
            int i19 = i18 * i15;
            int i20 = i9 / i19;
            int i21 = i20 * i15;
            if (i18 == 4) {
                if (i16 == 0) {
                    radb4(i20, i15, dArr, dArr4, dArr2, i13);
                } else {
                    radb4(i20, i15, dArr4, dArr, dArr2, i13);
                }
                i8 = 1 - i16;
            } else if (i18 == 2) {
                if (i16 == 0) {
                    radb2(i20, i15, dArr, dArr4, dArr2, i13);
                } else {
                    radb2(i20, i15, dArr4, dArr, dArr2, i13);
                }
                i8 = 1 - i16;
            } else if (i18 == 3) {
                if (i16 == 0) {
                    radb3(i20, i15, dArr, dArr4, dArr2, i13);
                } else {
                    radb3(i20, i15, dArr4, dArr, dArr2, i13);
                }
                i8 = 1 - i16;
            } else if (i18 == 5) {
                if (i16 == 0) {
                    radb5(i20, i15, dArr, dArr4, dArr2, i13);
                } else {
                    radb5(i20, i15, dArr4, dArr, dArr2, i13);
                }
                i8 = 1 - i16;
            } else {
                if (i16 == 0) {
                    i6 = i20;
                    i4 = i18;
                    i7 = 1;
                    i3 = i12;
                    radbg(i20, i18, i15, i21, dArr, dArr, dArr, dArr4, dArr4, dArr2, i13);
                } else {
                    i6 = i20;
                    i4 = i18;
                    i3 = i12;
                    i7 = 1;
                    radbg(i6, i4, i15, i21, dArr4, dArr4, dArr4, dArr, dArr, dArr2, i13);
                }
                i5 = i6;
                if (i5 == i7) {
                    i16 = 1 - i16;
                }
                i13 += (i4 - 1) * i5;
                i14 = i17;
                i15 = i19;
                i12 = i3;
                dArr3 = dArr2;
            }
            i16 = i8;
            i5 = i20;
            i4 = i18;
            i3 = i12;
            i13 += (i4 - 1) * i5;
            i14 = i17;
            i15 = i19;
            i12 = i3;
            dArr3 = dArr2;
        }
        if (i16 != 0) {
            for (int i22 = 0; i22 < i9; i22++) {
                dArr[i22] = dArr4[i22];
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void rfftf(int i, double[] dArr, double[] dArr2) {
        if (i != 1) {
            rfftf1(i, dArr, dArr2, 0);
        }
    }

    /* access modifiers changed from: package-private */
    public void rfftb(int i, double[] dArr, double[] dArr2) {
        if (i != 1) {
            rfftb1(i, dArr, dArr2, 0);
        }
    }

    /* access modifiers changed from: package-private */
    public void rffti1(int i, double[] dArr, int i2) {
        int i3;
        int i4;
        int i5 = i;
        int[] iArr = {4, 2, 3, 5};
        int i6 = i5;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        loop0:
        while (true) {
            i3 = 1;
            i7++;
            i8 = i7 <= 4 ? iArr[i7 - 1] : i8 + 2;
            while (true) {
                int i10 = i6 / i8;
                    i9++;
                    i4 = i5 * 2;
                    dArr[i9 + 1 + i4 + i2] = (double) i8;
                    if (i8 == 2 && i9 != 1) {
                        for (int i11 = 2; i11 <= i9; i11++) {
                            int i12 = (i9 - i11) + 2;
                            dArr[i12 + 1 + i4 + i2] = dArr[i12 + i4 + i2];
                        }
                        dArr[i4 + 2 + i2] = 2.0d;
                    }
                    if (i10 == 1) {
                        break loop0;
                    }
                    i6 = i10;

            }
        }
        double d = (double) i5;
        dArr[i4 + 0 + i2] = d;
        dArr[i4 + 1 + i2] = (double) i9;
        Double.isNaN(d);
        double d2 = 6.283185307179586d / d;
        int i13 = i9 - 1;
        if (i13 != 0) {
            int i14 = 1;
            int i15 = 1;
            int i16 = 0;
            while (i14 <= i13) {
                i14++;
                int i17 = (int) dArr[i14 + i4 + i2];
                int i18 = i15 * i17;
                int i19 = i5 / i18;
                int i20 = i17 - i3;
                int i21 = i16;
                int i22 = 1;
                int i23 = 0;
                while (i22 <= i20) {
                    int i24 = i23 + i15;
                    double d3 = (double) i24;
                    Double.isNaN(d3);
                    double d4 = d3 * d2;
                    double d5 = 0.0d;
                    double d6 = d2;
                    int i25 = i21;
                    for (int i26 = 3; i26 <= i19; i26 += 2) {
                        i25 += 2;
                        d5 += 1.0d;
                        double d7 = d5 * d4;
                        dArr[(i25 - 2) + i5 + i2] = Math.cos(d7);
                        dArr[(i25 - 1) + i5 + i2] = Math.sin(d7);
                    }
                    i21 += i19;
                    i22++;
                    i23 = i24;
                    d2 = d6;
                    i3 = 1;
                }
                i15 = i18;
                i16 = i21;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void rffti(int i, double[] dArr) {
        if (i != 1) {
            rffti1(i, dArr, 0);
        }
    }
}