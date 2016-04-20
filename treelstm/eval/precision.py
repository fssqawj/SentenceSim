# -*- coding: utf-8 -*-
import sys
import math


def calp(preres, relres, eps):
    return map(lambda x, y: y == (1.0 if x > eps else 0.0), preres, relres).count(True) * 1.0 / len(preres)


def read(resfile):
    return [float(x) for x in open(resfile, 'r').readlines()]


if __name__ == '__main__':
    # print calp([0.1, 0.2, 0.6, 0.9], [1.0, 0.0, 1.0, 1.0])
    print calp(read(sys.argv[1]), read(sys.argv[2]), float(sys.argv[3]))
