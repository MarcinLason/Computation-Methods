import numpy as np
import numpy.linalg as la
import sys
import scipy.sparse.linalg as spl

amountOfWords = int(sys.argv[1])
amountOfFiles = int(sys.argv[2])
amountOfResults = int(sys.argv[3])
svd = bool(sys.argv[4])
rank = int(sys.argv[5])

query = [0 for x in range(amountOfWords)]
matrix = [[0 for x in range(amountOfFiles)] for y in range(amountOfWords)]

def column(mat, ind):
    return [row[ind] for row in mat]

def closer(cor, arr):
    for ind in range(len(arr)):
        if cor > arr[ind][1]:
            return ind
    return -1

def best(matr, number):

    files = [[0,0] for x in range(number)]
    q1 = query / la.norm(query)

    if(svd):
        A = doSVD(matr,rank)
    else:
        A = matr

    for j in range(len(A[0])):
        dj = column(A, j)
        dj1 = dj/la.norm(dj)
        corj = np.dot(q1,dj1)

        if j < amountOfResults:
            files[j]=[j, corj]
        else:
            replace = closer(corj,files)
            if replace != -1:
                files[replace] = [j,corj]

    files.sort(key=lambda x: x[1])
    files.reverse()
    return [a for [a,b] in files]


def doSVD(A,rank):
    u,s,v= spl.svds(A,rank)
    s2=s
    for i in range(rank + 1, len(s2)):
        s2[i] = 0
    return (u.dot(np.diag(s2)).dot(v))

fo = open("matrix.txt", "r")
for ln in fo:
    ln = ln.split(" ")
    i=int(ln[0])
    j=int(ln[1])
    val= float(ln[2])
    matrix[i][j]=val
fo.close()

f = open("query.txt","r")
i=0
for ln in f:
    ln = ln.split(" ")
    query[i]= int(ln[0])
    i+=1
f.close()

fm = open("results.txt","w")
l = best(matrix, amountOfResults)
for i in range(amountOfResults):
    fm.write(str(l[i]) + " ")
fm.close()
