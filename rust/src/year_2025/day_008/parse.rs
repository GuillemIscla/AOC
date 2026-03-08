use crate::year_2025::day_008::junction_box::JunctionBox;
use std::fs::File;
use std::io::{self, Read};

pub fn parse(sample: Option<i32>) -> (usize, Vec<JunctionBox>) {
    parse_internal(sample).expect("Problems with parsing")
}

fn parse_internal(sample: Option<i32>) -> io::Result<(usize, Vec<JunctionBox>)> {
    let mut content = String::new();
    let connections: usize;

    match sample {
        None => {
            let file_path = "inputs/year_2025/day_008.txt";
            let mut file: File = File::open(file_path)?;
            file.read_to_string(&mut content)?;
            if content.ends_with('\n') {
                content.pop();
            }
            connections = 1000;
            content = content.to_string();
        }

        Some(1) => {
            connections = 10;
            content = "162,817,812
57,618,57
906,360,560
592,479,940
352,342,300
466,668,158
542,29,236
431,825,988
739,650,466
52,470,668
216,146,977
819,987,18
117,168,530
805,96,715
346,949,466
970,615,88
941,993,340
862,61,35
984,92,344
425,690,689"
                .to_string();
        }
        _ => {
            return Err(io::Error::new(
                io::ErrorKind::InvalidInput,
                "Sample received number is wrong",
            ))
        }
    }

    let junction_boxes = content
        .lines()
        .map(JunctionBox::new)
        .collect::<io::Result<Vec<JunctionBox>>>()?;

    Ok((connections, junction_boxes))
}
