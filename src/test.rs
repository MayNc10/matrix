use super::*;

const IDENTITY: [[f64; 2]; 2] = 
[[1.0, 0.0],
 [0.0, 1.0]];

#[test]
fn identity() {
    assert!(Matrix::new(IDENTITY) == Matrix::<2, 2>::identity())
}
#[test]
fn det1x1() {
    let mut mat = Matrix::new([[rand::random::<f64>()]]);
    assert_eq!(mat[(0,0)], mat.det());
    mat = Matrix::new([[1.0]]);
    assert_eq!(mat.det(), 1.0);
    mat = Matrix::new([[0.0]]);
    assert_eq!(mat.det(), 0.0);
}
#[test]
fn det2x2() {
    let mut mat = Matrix::new([[1.0, 1.0], [1.0, 1.0]]);
    assert_eq!(mat.det(), 0.0);
    mat = Matrix::<2, 2>::identity();
    assert_eq!(mat.det(), 1.0);
    mat = Matrix::new([[5.0, 3.0], [1.0, 2.0]]);
    assert_eq!(mat.det(), 7.0);
}
#[test]
fn det3x3() {
    let mut mat = Matrix::new([[1.0, 1.0, 1.0], [1.0, 1.0, 1.0], [1.0, 1.0, 1.0]]);
    assert_eq!(mat.det(), 0.0);
    mat = Matrix::<3, 3>::identity();
    assert_eq!(mat.det(), 1.0);
    mat = Matrix::new([[5.0, 3.0, 4.0], [1.0, 2.0, 6.0], [9.0, 7.0, 8.0]]);
    assert_eq!(mat.det(), -36.0);
}

