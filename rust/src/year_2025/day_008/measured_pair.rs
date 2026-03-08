#[derive(Copy, Clone, Debug)]
pub struct MeasuredPair {
    pub one: usize,
    pub two: usize,
    pub dist_square: i64,
}

impl MeasuredPair {
    pub fn contains(&self, index: usize) -> bool {
        self.one == index || self.two == index
    }
}
