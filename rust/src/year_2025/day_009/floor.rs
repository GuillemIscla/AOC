use crate::year_2025::day_009::tile::Tile;
use crate::year_2025::day_009::tile_color::TileColor;
use std::fmt;
use std::io;

pub struct Floor {
    map: Vec<Vec<TileColor>>,
    scale_x: Vec<usize>,
    scale_y: Vec<usize>,
}

impl fmt::Display for Floor {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        if let Some(max_x) = self.scale_x.last() && let Some(max_y) = self.scale_y.last() {
            let mut max_digits_x = 0;
            let mut remainder_x = *max_x;
            while remainder_x > 0 {
                remainder_x /= 10;
                max_digits_x += 1;
            }
            let mut max_digits_y = 0;
            let mut remainder_y = *max_y;
            while remainder_y > 0 {
                remainder_y /= 10;
                max_digits_y += 1;
            }

            for i in (0..max_digits_x).rev() {
                let spaces = " ".repeat(max_digits_y + 1);
                write!(f, "{ }", spaces)?;
                for j in self.scale_x.iter() {
                    if *j < 10_usize.pow(i) {
                        write!(f, " ")?;
                    } else {
                        let value = (j / 10_usize.pow(i)) % 10;
                        write!(f, "{}", value)?;
                    }
                }
                writeln!(f)?;
            }
            let rows = self.map.len();
            let cols = self.map.first().map_or(0, |r| r.len());

            for c in 0..cols {
                write!(f, "{:>width$} ", self.scale_y[c], width = max_digits_y)?;
                for r in 0..rows {
                    write!(f, "{}", self.map[r][c].as_char())?;
                }
                writeln!(f)?;
            }
            write!(f, "{:>width$} ", max_y, width = max_digits_y)?;
        }

        Ok(())
    }
}

impl Floor {
    pub fn new(tiles: &[Tile]) -> Self {
        let scale_x_min = tiles
            .iter()
            .map(|Tile(a, _)| a)
            .min()
            .unwrap()
            .checked_sub_signed(1)
            .unwrap();
        let scale_x_max = *tiles.iter().map(|Tile(a, _)| a).max().unwrap() + 2;
        let scale_y_min = tiles
            .iter()
            .map(|Tile(_, b)| b)
            .min()
            .unwrap()
            .checked_sub_signed(1)
            .unwrap();
        let scale_y_max = *tiles.iter().map(|Tile(_, b)| b).max().unwrap() + 2;

        let mut floor = Floor {
            map: vec![vec![TileColor::NotPainted]],
            scale_x: vec![scale_x_min, scale_x_max],
            scale_y: vec![scale_y_min, scale_y_max],
        };

        tiles.iter().for_each(|tile| {
            floor.insert_rectangle(tile, tile);
            let _ = floor.paint_area(tile, tile, TileColor::Red);
        });

        tiles.iter().enumerate().for_each(|(index, tile)| {
            let next_tile = &tiles[(index + 1) % tiles.len()];
            Self::find_inner_rectangle(tile, next_tile)
                .iter()
                .for_each(|(top, bottom)| {
                    let _ = floor.paint_area(top, bottom, TileColor::Green);
                });
        });
       //Paint outer color with Neither
       let mut to_visit: Vec<(usize, usize)> = vec![(0, 0)];
       let mut visited: Vec<(usize, usize)> = Vec::new();
       while let Some(visiting) = to_visit.pop() {
           visited.push(visiting);
           floor.map[visiting.0][visiting.1] = TileColor::Neither;
           let candidates = vec![
               (visiting.0.checked_sub_signed(1), visiting.1.checked_sub_signed(1)),
               (visiting.0.checked_sub_signed(1), Some(visiting.1)),
               (visiting.0.checked_sub_signed(1), Some(visiting.1 + 1)),
               (Some(visiting.0), visiting.1.checked_sub_signed(1)),
               (Some(visiting.0), Some(visiting.1)),
               (Some(visiting.0), Some(visiting.1 + 1)),
               (Some(visiting.0 + 1), visiting.1.checked_sub_signed(1)),
               (Some(visiting.0 + 1), Some(visiting.1)),
               (Some(visiting.0 + 1), Some(visiting.1 + 1)),
           ];
           candidates.into_iter().flat_map(|(opt_x, opt_y)| {
               if let Some(x) = opt_x && let Some(y) = opt_y {
                   Some((x,y))
               }
               else {
                   None
               }
           }).for_each(|c| {
               if c.0 < floor.scale_x.len() -1 && c.1 < floor.scale_y.len() -1 && floor.map[c.0][c.1] == TileColor::NotPainted {
                   to_visit.push(c)
               }
           });
       }
    
       //Resolve no color with green
       (0..floor.map.len()).for_each(|i| {
           (0..floor.map[i].len()).for_each(|j| {
               if floor.map[i][j] == TileColor::NotPainted {
                   floor.map[i][j] = TileColor::Green;
               }
           });
       });
    
        floor
    }

    fn find_inner_rectangle(a: &Tile, b: &Tile) -> Option<(Tile, Tile)> {
        if a == b {
            None
        } else if a.0 == b.0 {
            Some((
                Tile(a.0, std::cmp::min(a.1, b.1) + 1),
                Tile(a.0, std::cmp::max(a.1, b.1).checked_sub(1).unwrap()),
            ))
        } else if a.1 == b.1 {
            Some((
                Tile(std::cmp::min(a.0, b.0) + 1, a.1),
                Tile(std::cmp::max(a.0, b.0).checked_sub(1).unwrap(), a.1),
            ))
        } else {
            Some((
                Tile(std::cmp::min(a.0, b.0) + 1, std::cmp::min(a.1, b.1) + 1),
                Tile(
                    std::cmp::max(a.0, b.0).checked_sub(1).unwrap(),
                    std::cmp::max(a.1, b.1).checked_sub(1).unwrap(),
                ),
            ))
        }
    }

    fn insert_rectangle(&mut self, top: &Tile, bottom: &Tile) {
        self.insert_line(top.0, true);
        self.insert_line(top.1, false);
        self.insert_line(bottom.0 + 1, true);
        self.insert_line(bottom.1 + 1, false);
    }

    fn insert_line(&mut self, index: usize, is_horizontal: bool) {
        if is_horizontal {
            let pos = match self
                .scale_x
                .iter()
                .enumerate()
                .find(|(_, x)| **x >= index)
                .map(|(pos, _)| pos)
            {
                Some(pos) if self.scale_x[pos] != index => pos,
                _ => return,
            };
            self.scale_x.insert(pos, index);
            let pos_in_map = pos.checked_sub(1).unwrap();
            let new_line = self.map[pos_in_map].clone();
            self.map.insert(pos_in_map, new_line);
        } else {
            let pos = match self
                .scale_y
                .iter()
                .enumerate()
                .find(|(_, y)| **y >= index)
                .map(|(pos, _)| pos)
            {
                Some(pos) if self.scale_y[pos] != index => pos,
                _ => return,
            };
            self.scale_y.insert(pos, index);
            for i in 0..self.map.len() {
                let pos_in_map = pos.checked_sub(1).unwrap();
                let value = self.map[i][pos_in_map].clone();
                self.map[i].insert(pos_in_map, value);
            }
        }
    }

    fn paint_area(&mut self, top: &Tile, bottom: &Tile, color: TileColor) -> io::Result<()> {
        let min_x = self
            .scale_x
            .iter()
            .enumerate()
            .find(|(_, x)| **x == top.0)
            .map(|(pos, _)| pos)
            .ok_or({
                io::Error::new(
                    io::ErrorKind::InvalidData,
                    format!(
                        "top.0 '{}' has a value I cannot find in scale_x '{:?}' ",
                        top.0, self.scale_x
                    ),
                )
            })?;

        let max_x = self
            .scale_x
            .iter()
            .enumerate()
            .find(|(_, x)| **x == bottom.0 + 1)
            .map(|(pos, _)| pos)
            .ok_or({
                io::Error::new(
                    io::ErrorKind::InvalidData,
                    format!(
                        "bottom.0 + 1 '{}' has a value I cannot find in scale_x '{:?}' ",
                        bottom.0 + 1,
                        self.scale_x
                    ),
                )
            })?;

        let min_y = self
            .scale_y
            .iter()
            .enumerate()
            .find(|(_, y)| **y == top.1)
            .map(|(pos, _)| pos)
            .ok_or({
                io::Error::new(
                    io::ErrorKind::InvalidData,
                    format!(
                        "top.1 '{}' has a value I cannot find in scale_y '{:?}' ",
                        top.1, self.scale_y
                    ),
                )
            })?;

        let max_y = self
            .scale_y
            .iter()
            .enumerate()
            .find(|(_, y)| **y == bottom.1 + 1)
            .map(|(pos, _)| pos)
            .ok_or({
                io::Error::new(
                    io::ErrorKind::InvalidData,
                    format!(
                        "bottom.1 + 1 '{}' has a value I cannot find in scale_y '{:?}' ",
                        bottom.1 + 1,
                        self.scale_y
                    ),
                )
            })?;

        for x in min_x..max_x {
            for y in min_y..max_y {
                self.map[x][y] = color.clone();
            }
        }

        Ok(())
    }

    pub fn rectangle_valid(&self, a: &Tile, b: &Tile) -> io::Result<bool> {
        let top = Tile(std::cmp::min(a.0, b.0), std::cmp::min(a.1, b.1));
        let bottom = Tile(std::cmp::max(a.0, b.0), std::cmp::max(a.1, b.1));
        let min_x = self
            .scale_x
            .iter()
            .enumerate()
            .find(|(_, x)| **x == top.0)
            .map(|(pos, _)| pos)
            .ok_or({
                io::Error::new(
                    io::ErrorKind::InvalidData,
                    format!(
                        "top.0 '{}' has a value I cannot find in scale_x '{:?}' ",
                        top.0, self.scale_x
                    ),
                )
            })?;

        let max_x = self
            .scale_x
            .iter()
            .enumerate()
            .find(|(_, x)| **x == bottom.0 + 1)
            .map(|(pos, _)| pos)
            .ok_or({
                io::Error::new(
                    io::ErrorKind::InvalidData,
                    format!(
                        "bottom.0 + 1 '{}' has a value I cannot find in scale_x '{:?}' ",
                        bottom.0 + 1,
                        self.scale_x
                    ),
                )
            })?;

        let min_y = self
            .scale_y
            .iter()
            .enumerate()
            .find(|(_, y)| **y == top.1)
            .map(|(pos, _)| pos)
            .ok_or({
                io::Error::new(
                    io::ErrorKind::InvalidData,
                    format!(
                        "top.1 '{}' has a value I cannot find in scale_y '{:?}' ",
                        top.1, self.scale_y
                    ),
                )
            })?;

        let max_y = self
            .scale_y
            .iter()
            .enumerate()
            .find(|(_, y)| **y == bottom.1 + 1)
            .map(|(pos, _)| pos)
            .ok_or({
                io::Error::new(
                    io::ErrorKind::InvalidData,
                    format!(
                        "bottom.1 + 1 '{}' has a value I cannot find in scale_y '{:?}' ",
                        bottom.1 + 1,
                        self.scale_y
                    ),
                )
            })?;

        for x in min_x..max_x {
            for y in min_y..max_y {
                if self.map[x][y] == TileColor::Neither {
                    return Ok(false);
                }
            }
        }

        Ok(true)
    }
}

