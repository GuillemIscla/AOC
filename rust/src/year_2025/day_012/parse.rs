use crate::year_2025::day_012::present::Present;
use std::fs::File;
use std::io::{self, Read};

use crate::year_2025::day_012::tree_area::TreeArea;

pub fn parse(sample: Option<i32>) -> (Vec<Present>, Vec<TreeArea>) {
    parse_internal(sample).expect("Problems with parsing")
}

fn parse_internal(sample: Option<i32>) -> io::Result<(Vec<Present>, Vec<TreeArea>)> {
    let mut content = String::new();

    match sample {
        None => {
            let file_path = "inputs/year_2025/day_012.txt";
            let mut file: File = File::open(file_path)?;
            file.read_to_string(&mut content)?;
            if content.ends_with('\n') {
                content.pop();
            }
            content = content.to_string();
        }

        Some(1) => {
            content = "0:
###
##.
##.

1:
###
##.
.##

2:
.##
###
##.

3:
##.
###
##.

4:
###
#..
###

5:
###
.#.
###

4x4: 0 0 0 0 2 0
12x5: 1 0 1 0 2 2
12x5: 1 0 1 0 3 2"
                .to_string();
        }
        _ => {
            return Err(io::Error::new(
                io::ErrorKind::InvalidInput,
                "Sample received number is wrong",
            ))
        }
    }

    let mut lines = content.lines().collect::<Vec<&str>>();

    let tree_areas_raw = lines.split_off(30);
    let presents_raw = lines;

    let mut presents = Vec::new();

    for present_number in 0..=5 {
        let index = 1 + present_number * 5;
        presents.push(Present::new(&presents_raw[index..(index + 3)])?);
    }

    let tree_areas = tree_areas_raw
        .into_iter()
        .map(TreeArea::new)
        .collect::<io::Result<Vec<TreeArea>>>()?;

    Ok((presents, tree_areas))
}
