use super::id_database::IdDatabase;
use std::fs::File;
use std::io::{self, Read};

pub fn parse(sample: Option<i32>) -> (IdDatabase, Vec<i64>) {
    parse_internal(sample).expect("Problems with parsing")
}

fn parse_internal(sample: Option<i32>) -> io::Result<(IdDatabase, Vec<i64>)> {
    let mut content = String::new();

    match sample {
        None => {
            let file_path = "inputs/year_2025/day_005.txt";
            let mut file: File = File::open(file_path)?;
            file.read_to_string(&mut content)?;
        }

        Some(1) => {
            content = "3-5
10-14
16-20
12-18

1
5
8
11
17
32"
            .to_string();
        }
        Some(2) => {
            content = "3-5
10-14
16-20
13-14
12-18

1
5
8
11
17
32"
            .to_string();
        }
        _ => {
            return Err(io::Error::new(
                io::ErrorKind::InvalidInput,
                "Sample received number is wrong",
            ))
        }
    }

    parse_logic(&content)
}

fn parse_logic(s: &str) -> io::Result<(IdDatabase, Vec<i64>)> {
    let mut parts = s.split("\n\n");

    let first = parts
        .next()
        .ok_or_else(|| io::Error::new(io::ErrorKind::InvalidInput, "Missing database values"))?;

    let id_database = IdDatabase::new(first.to_string())?;

    let second = parts
        .next()
        .ok_or_else(|| io::Error::new(io::ErrorKind::InvalidInput, "Missing ids"))?;

    if parts.next().is_some() {
        return Err(io::Error::new(
            io::ErrorKind::InvalidInput,
            "Too many parts",
        ));
    }

    let ids = second
        .split("\n")
        .filter(|s| !s.is_empty())
        .map(|id_raw| {
            id_raw
                .parse::<i64>()
                .map_err(|_| io::Error::new(io::ErrorKind::InvalidInput, "Invalid second number"))
        })
        .collect::<io::Result<Vec<i64>>>()?;

    Ok((id_database, ids))
}
