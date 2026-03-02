use std::io;

#[derive(Debug)]
pub enum Operation {
    Sum,
    Mult,
}

#[derive(Debug)]
pub struct Homework {
    pub lines: Vec<Vec<Option<i64>>>,
    pub operations: Vec<Operation>,
}

impl Homework {
    pub fn new(s: String, cephalopod_reading: bool) -> io::Result<Homework> {
        let parts: Vec<&str> = s.split("\n").collect();
        let lines_raw = &parts[0..parts.len() - 1];
        let operations_raw = &parts[parts.len() - 1];
        let lines: Vec<Vec<Option<i64>>> = if cephalopod_reading {
            Self::read_as_cephalopod(lines_raw.to_vec(), operations_raw)?
        } else {
            Self::read_as_human(lines_raw.to_vec())?
        };

        let operations = operations_raw
            .split_whitespace()
            .map(|operation_raw| {
                if operation_raw == "+" {
                    Ok(Operation::Sum)
                } else if operation_raw == "*" {
                    Ok(Operation::Mult)
                } else {
                    Err(io::Error::new(
                        io::ErrorKind::InvalidInput,
                        "Not an operation",
                    ))
                }
            })
            .collect::<io::Result<Vec<Operation>>>()?;
        Ok(Homework { lines, operations })
    }

    pub fn read_as_human(lines_raw: Vec<&str>) -> io::Result<Vec<Vec<Option<i64>>>> {
        lines_raw
            .iter()
            .map(|line_raw| {
                line_raw
                    .split_whitespace()
                    .map(|s| {
                        s.parse::<i64>()
                            .map_err(|_| {
                                io::Error::new(
                                    io::ErrorKind::InvalidInput,
                                    format!("{} is not a number", s),
                                )
                            })
                            .map(Option::Some)
                    })
                    .collect::<io::Result<Vec<Option<i64>>>>()
            })
            .collect::<io::Result<Vec<Vec<Option<i64>>>>>()
    }

    pub fn read_as_cephalopod(
        lines_raw: Vec<&str>,
        operations_raw: &str,
    ) -> io::Result<Vec<Vec<Option<i64>>>> {
        let mut column_sizes: Vec<usize> = Vec::new();
        let mut acc = 0;

        for c in operations_raw.chars() {
            if c != ' ' {
                if acc > 0 {
                    column_sizes.push(acc);
                }
                acc = 0;
            } else {
                acc += 1;
            }
        }

        column_sizes.push(acc + 1);

        let lines_str: Vec<Vec<String>> = lines_raw
            .iter()
            .map(|line_raw| {
                let mut line_raw_copy = line_raw.to_string();
                let mut line_str: Vec<String> = Vec::new();
                for column_size in column_sizes.clone() {
                    line_str.push(line_raw_copy.chars().take(column_size).collect());
                    line_raw_copy = line_raw_copy.chars().skip(column_size + 1).collect();
                }
                line_str
            })
            .collect();

        let mut lines: Vec<Vec<Option<i64>>> = Vec::new();

        for _ in 0..lines_raw.len() {
            lines.push(Vec::new())
        }

        for (index, column_size) in column_sizes.into_iter().enumerate() {
            (0..lines_raw.len()).for_each(|index_str| {
                if index_str < column_size {
                    let mut acc = 0;
                    (0..lines_raw.len()).for_each(|j| {
                        lines_str[j][index]
                            .chars()
                            .nth(index_str)
                            .iter()
                            .for_each(|char| {
                                char.to_string().parse::<i64>().iter().for_each(|digit| {
                                    acc *= 10;
                                    acc += digit;
                                });
                            });
                    });
                    lines[index_str].push(Some(acc));
                } else {
                    lines[index_str].push(None);
                }
            });
        }

        Ok(lines)
    }

    pub fn operate(&self) -> Vec<i64> {
        self.operations
            .iter()
            .enumerate()
            .map(|(index, operation)| {
                let column = self.lines.iter().flat_map(|line| line[index]);
                match operation {
                    Operation::Sum => column.sum(),
                    Operation::Mult => {
                        let mut acc = 1;
                        column.for_each(|value| acc *= value);
                        acc
                    }
                }
            })
            .collect()
    }
}
