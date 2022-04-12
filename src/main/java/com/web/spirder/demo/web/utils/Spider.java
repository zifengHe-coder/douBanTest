package com.web.spirder.demo.web.utils;

import org.junit.Test;

public class Spider {
    @Test
    public void test() {
        int[] n = new int[]{1,6,3,8,33,27,66,9,7,88};
        int temp,index=-1;
        for (int i = 0; i<n.length - 1; i++) {
            index = i;
            for (int j = i + 1; j < n.length; j++) {
                if (n[index] > n[j]) index = j;
            }
            if (index > 0) {
                temp = n[i];
                n[i] = n[index];
                n[temp] = temp;
            }
        }

        for (int i = 0; i < n.length - 1; i++) {
            for (int j = 0; j < n.length -1; j++) {
                if (n[j] > n[j+1]) {
                    temp = n[j];
                    n[j] = n[j+1];
                    n[j+1] = temp;
                }
            }
        }

        temp = 0;
        int j;
        for (int i = 1; i < n.length; i++) {
            temp = n[i];
            for (j = i; j > 0; j--) {
                if (n[j-1] > temp) {
                    n[j] = n[j-1];
                    if (j == 1) {
                        n[j-1] = temp;
                        break;
                    }
                } else {
                    n[j] = temp;
                    break;
                }
            }
        }

        f(n, 0, n.length - 1);

        merge(n, 0, n.length - 1);

        radixSort(n);

        shellSort(n);

        int length = n.length;
        for (int i = length/2 - 1; i >= 0; i--) {
            maximumHeap(i, n, length);
        }
        for (int i = n.length - 1; i >= 0; i--) {
            swap(n, 0, i);
            length--;
            maximumHeap(0, n, length);
        }
    }

    public static void f(int[] arr, int start, int end) {
        if (start < end) {
            int left = start;
            int right = end;
            int temp = arr[start];

            while (left < right) {
                while (left < right && arr[right] > temp) {
                    right--;
                }
                if (left < right) {
                    arr[left] = arr[right];
                    left++;
                }
                while (left < right && arr[left] <= temp) {
                    left++;
                }
                arr[right] = arr[left];
            }
            arr[left] = temp;
            f(arr, start, left);
            f(arr, left + 1, end);
        }
    }

    public static void merge(int[] arr, int low, int high) {
        int center = (high + low) /2;
        if (low < high) {
            merge(arr, low, center);
            merge(arr, center + 1, high);
            mergeSort(arr, low, center, high);
        }
    }

    public static void mergeSort(int[] arr, int low, int center, int high) {
        int[] tempArr = new int[arr.length];
        int i = low, j = center + 1;
        int index = 0;
        while (i <= center && j <= high) {
            if (arr[i] < arr[j]) {
                tempArr[index] = arr[i];
                i++;
            } else {
                tempArr[index] = arr[j];
                j++;
            }
            index++;
        }
    }

    public static void radixSort(int[] arr) {
        int maxLength = 0;
        for (int i = 0; i < arr.length; i++) {
            if (maxLength < arr[i])
                maxLength = arr[i];
        }
        maxLength = (maxLength + "").length();
        int[][] temp = new int[10][arr.length];
        int[] counts = new int[10];
        int num = 0;
        int index = 0;
        for (int i = 0,n = 1; i < maxLength; i++,n*=10) {
            for (int j = 0; j < arr.length; j++) {
                num = arr[j]/n%10;
                temp[num][counts[num]] = arr[j];
                counts[num]++;
            }
        }
        for (int j = 0; j < counts.length; j++) {
            for (int j2 = 0; j2 < counts[j]; j2++) {
                arr[index] = temp[j][j2];
                index++;
            }
            counts[j] = 0;
        }
        index = 0;
    }

    public static void shellSort(int[] arr) {
        int temp;
        for (int i = arr.length/2; i > 0; i /= 2) {
            for (int j = i; j < arr.length; j++) {
                for (int k = j - i; k >= 0; k -= i) {
                    if (arr[k] > arr[k+i]) {
                        temp = arr[k];
                        arr[k] = arr[k+i];
                        arr[k+i] = temp;
                    }
                }
            }
        }
    }

    public static void maximumHeap(int i, int[] arr, int length) {
        int temp = arr[i];
        for (int j = i*2+1; j < length; j=j*2+1) {
            if (j+1<length && arr[j+1] > arr[j]) {
                j++;
            }
            if (arr[j] > temp) {
                arr[i] = arr[j];
                i = j;
            } else {
                break;
            }
        }
        arr[i] = temp;
    }


    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
