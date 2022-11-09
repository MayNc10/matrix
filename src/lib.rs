#![allow(incomplete_features)]
#![feature(generic_const_exprs)]

use std::{ops::{Index, IndexMut}, fmt::{Display, Debug}};

#[cfg(test)]
mod test;

mod expr_utils;
use expr_utils::*;

pub struct Matrix<const N: usize, const M: usize> {
    inner: [[f64; M]; N] // Outer is rows
}

const WIDTH: usize = 3;

impl<const N: usize, const M: usize> Matrix<N, M> {
    pub fn zero() -> Matrix<N, M> {
        Matrix { inner: [[0.0; M]; N] }
    }
    pub fn identity() -> Matrix<N, N> {
        let mut base = [[0.0; N]; N];
        for i in 0..N {
            base[i][i] = 1.0;
        }
        Matrix { inner: base }
    }
    pub fn new(inner: [[f64; M]; N]) -> Matrix<N, M> {
        Matrix { inner }
    }
    pub fn new_with_initializer(init: f64) -> Matrix<N, M> {
        Matrix { inner: [[init; M]; N] }
    }
}

impl<const N: usize, const M: usize> PartialEq for Matrix<N, M> {
    fn eq(&self, other: &Self) -> bool {
        self.inner == other.inner
    }
    fn ne(&self, other: &Self) -> bool {
        self.inner != other.inner
    }
}

impl<const N: usize, const M: usize> Index<usize> for Matrix<N, M> {
    type Output = [f64; M];
    fn index(&self, index: usize) -> &Self::Output {
        &self.inner[index]
    }
}

impl<const N: usize, const M: usize> IndexMut<usize> for Matrix<N, M> {
    fn index_mut(&mut self, index: usize) -> &mut Self::Output {
        &mut self.inner[index]
    }
}

impl<const N: usize, const M: usize> Index<(usize, usize)> for Matrix<N, M> {
    type Output = f64;
    fn index(&self, index: (usize, usize)) -> &Self::Output {
        &self.inner[index.0][index.1]
    }
}

impl<const N: usize, const M: usize> IndexMut<(usize, usize)> for Matrix<N, M> {
    fn index_mut(&mut self, index: (usize, usize)) -> &mut Self::Output {
        &mut self.inner[index.0][index.1]
    }
}

impl<const N: usize, const M: usize> Display for Matrix<N, M> {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        for row in self.inner {
            for col_idx in 0..M {
                if col_idx == 0 {
                    write!(f, "|")?;
                }
                write!(f, "{:WIDTH$} ", row[col_idx])?;
                if col_idx == M - 1 {
                    writeln!(f, "|")?;
                }
            }
        }
        Ok(())
    }
}
 
impl<const N: usize, const M: usize> Debug for Matrix<N, M> { 
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        write!(f, "{}", self)
    }
}

trait Deteriminant { fn det(&self) -> f64; }

impl Deteriminant for Matrix<1,1> {
    fn det(&self) -> f64 {
        self.inner[0][0]
    }
}

impl Deteriminant for Matrix<2,2> {
    fn det(&self) -> f64 {
        self.inner[0][0] * self.inner[1][1] - self.inner[1][0] * self.inner[0][1]
    }
}

impl<const N: usize>  Deteriminant for  Matrix<N, N> 
where If::<{N > 2}> : True,
Matrix<{N - 1}, {N - 1}> : Deteriminant,
{
    fn det(&self) -> f64 {
        let mut det = 0.0;
        for i in 0..N {
            // First, reduce the matrix to a N - 1 x N - 1
            let mut new_mat = [[0.0; N - 1]; N - 1];
            for row in 1..N {
                for col in 0..N {
                    if col == i {
                        continue;
                    }
                    let mut col_idx = col;
                    if col > i {
                        col_idx -= 1;
                    }
                    let row_idx = row - 1;
                    new_mat[row_idx][col_idx] = self.inner[row][col];
                }
            }
            let new_mat = Matrix::new(new_mat);
            let scalar = if i % 2 == 1 {
                self.inner[0][i] * -1.0
            } else {
                self.inner[0][i]
            };
            det += scalar * new_mat.det();
        }
        det
    
    }
}

pub fn mul_row(r: &[f64], scale: f64, new_r: &[f64]) {

}

impl<const N: usize, const M: usize> Matrix<N, M> {
    pub fn swap(&mut self, mut r1: usize, mut r2: usize) {
        if r1 == r2 {
            return;
        } else if r2 < r1 {
            (r1, r2) = (r2, r1);
        }


        let (r1_slice, r2_slice) = self.inner.split_at_mut(r1 + 1);
        r1_slice[r1..r1 + 1].swap_with_slice(&mut r2_slice[r2 - r1..r2 - r1 + 1]);
    }
    
    pub fn mul_row_by_idx(&mut self, r: usize, d: f64) {
        for entry in &mut self.inner[r] {
            *entry *= d;
        }
    }
    pub fn div_row_by_idx(&mut self, r: usize, d: f64) {
        for entry in &mut self.inner[r] {
            *entry /= d;
        }
    }
    pub fn add_row(&mut self, r1: usize, r2: &[f64; M]) {
        for idx in 0..M {
            self[(r1, idx)] += r2[idx];
        }
    } 
    pub fn sub_row(&mut self, r1: usize, r2: &[f64; M]) {
        for idx in 0..M {
            self[(r1, idx)] -= r2[idx];
        }
    }
    pub fn add_row_by_idx(&mut self, r1: usize, r2: usize) {
        for idx in 0..M {
            self[(r1, idx)] += self[(r2, idx)];
        }
    } 
    pub fn sub_row_by_idx(&mut self, r1: usize, r2: usize) {
        for idx in 0..M {
            self[(r1, idx)] -= self[(r2, idx)];
        }
    }

    pub fn to_rref(&mut self) {
        let mut lead = 0;
        for r in 0..N {
            if lead > M {
                return;
            }
            let mut i = r;
            while self[(i, lead)] == 0.0 {
                i += 1;
                if i == N {
                    i = r;
                    lead += 1;
                    if lead == M {
                        return;
                    }
                }
            }
            if i != r {
                self.swap(i, r);
            }
            // divide row r by M[r, lead]
            self.div_row_by_idx(r, self[(r, lead)]);
            for j in 0..N {
                if j != r {
                    //Subtract M[j, lead] multiplied by row r from row j
                    self.sub_row(j, )
                }
            }
            lead += 1;
        }
    }
}
