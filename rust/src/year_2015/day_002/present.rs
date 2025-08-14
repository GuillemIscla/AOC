use std::cmp;

#[derive(Debug)]
pub struct Present {
    pub l:u32,
    pub w:u32,
    pub h:u32,
}

impl Present {

    pub fn new (l: u32, w: u32, h: u32) -> Present {
        Present { l, w, h }
    }
    pub fn required_paper(&self) -> u32 {
        2 * self.l * self.w +
            2 * self.w * self.h +
            2 * self.h * self.l
    }

    pub fn smaller_sides(&self) -> (u32, u32) {
        let max = cmp::max(cmp::max(self.l, self.w), self.h);
        if max == self.l {
            (self.w, self.h)
        }
        else if max == self.w {
            (self.h, self.l)
        }
        else {
            (self.l, self.w)
        }
    }
}