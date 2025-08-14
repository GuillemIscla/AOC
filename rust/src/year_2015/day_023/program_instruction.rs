use regex::Regex;
use std::fmt::Display;
use std::io::{self, Error, ErrorKind};

#[derive(Debug)]
pub enum ProgramInstruction {
    Hlf { r: String },
    Tpl { r: String },
    Inc { r: String },
    Jmp { offset: isize },
    Jie { r: String, offset: isize },
    Jio { r: String, offset: isize },
}

impl ProgramInstruction {
    pub fn new(raw: &str) -> io::Result<ProgramInstruction> {
        Self::new_modify(raw)
            .or_else(|_| Self::new_jump_1(raw))
            .or_else(|_| Self::new_jump_2(raw))
            .map_err(|_| Error::new(ErrorKind::InvalidData, format!("Invalid line: {}", raw)))
    }

    fn new_modify(raw: &str) -> io::Result<ProgramInstruction> {
        let regex_modify_raw = r"(hlf|tpl|inc) (a|b)";
        let regex_modify =
            Regex::new(regex_modify_raw).map_err(|e| Error::new(ErrorKind::InvalidInput, e))?;

        let captures_modify = regex_modify
            .captures(raw)
            .ok_or_else(|| Error::new(ErrorKind::InvalidData, format!("Invalid line: {}", raw)))?;

        match &captures_modify[1] {
            "hlf" => Ok(ProgramInstruction::Hlf {
                r: captures_modify[2].to_string(),
            }),
            "tpl" => Ok(ProgramInstruction::Tpl {
                r: captures_modify[2].to_string(),
            }),
            "inc" => Ok(ProgramInstruction::Inc {
                r: captures_modify[2].to_string(),
            }),
            _ => Err(Error::new(
                ErrorKind::InvalidData,
                format!("Invalid line: {}", raw),
            )),
        }
    }

    fn new_jump_1(raw: &str) -> io::Result<ProgramInstruction> {
        let regex_jump_1_raw = r"(jmp) ([\+,-][0-9]+)";
        let regex_jump_1 =
            Regex::new(regex_jump_1_raw).map_err(|e| Error::new(ErrorKind::InvalidInput, e))?;

        let captures_jump_1 = regex_jump_1
            .captures(raw)
            .ok_or_else(|| Error::new(ErrorKind::InvalidData, format!("Invalid line: {}", raw)))?;

        match &captures_jump_1[1] {
            "jmp" => Ok(ProgramInstruction::Jmp {
                offset: captures_jump_1[2].parse::<isize>().map_err(|_| {
                    Error::new(ErrorKind::InvalidData, format!("Invalid line: {}", raw))
                })?,
            }),
            _ => Err(Error::new(
                ErrorKind::InvalidData,
                format!("Invalid line: {}", raw),
            )),
        }
    }

    fn new_jump_2(raw: &str) -> io::Result<ProgramInstruction> {
        let regex_jump_2_raw = r"(jie|jio) (a|b), ([\+,-][0-9]+)";
        let regex_jump_2 =
            Regex::new(regex_jump_2_raw).map_err(|e| Error::new(ErrorKind::InvalidInput, e))?;

        let captures_jump_2 = regex_jump_2
            .captures(raw)
            .ok_or_else(|| Error::new(ErrorKind::InvalidData, format!("Invalid line: {}", raw)))?;

        match &captures_jump_2[1] {
            "jie" => Ok(ProgramInstruction::Jie {
                r: captures_jump_2[2].to_string(),
                offset: captures_jump_2[3].parse::<isize>().map_err(|_| {
                    Error::new(ErrorKind::InvalidData, format!("Invalid line: {}", raw))
                })?,
            }),
            "jio" => Ok(ProgramInstruction::Jio {
                r: captures_jump_2[2].to_string(),
                offset: captures_jump_2[3].parse::<isize>().map_err(|_| {
                    Error::new(ErrorKind::InvalidData, format!("Invalid line: {}", raw))
                })?,
            }),
            _ => Err(Error::new(
                ErrorKind::InvalidData,
                format!("Invalid line: {}", raw),
            )),
        }
    }

    //account for the register that they are defined to use
    pub fn process(&self, index: &mut isize, a: &mut usize, b: &mut usize) {
        match self {
            ProgramInstruction::Hlf { r } => {
                let reference: &mut usize = if r == "a" { a } else { b };
                *reference /= 2;
                *index += 1;
            }
            ProgramInstruction::Tpl { r } => {
                let reference: &mut usize = if r == "a" { a } else { b };
                *reference *= 3;
                *index += 1;
            }
            ProgramInstruction::Inc { r } => {
                let reference: &mut usize = if r == "a" { a } else { b };
                *reference += 1;
                *index += 1;
            }
            ProgramInstruction::Jmp { offset } => *index += offset,
            ProgramInstruction::Jie { r, offset } => {
                let reference: &mut usize = if r == "a" { a } else { b };
                if *reference % 2 == 0 {
                    *index += offset;
                } else {
                    *index += 1;
                }
            }
            ProgramInstruction::Jio { r, offset } => {
                let &mut ref mut reference = if r == "a" { a } else { b };
                if *reference == 1 {
                    *index += offset;
                } else {
                    *index += 1;
                }
            }
        }
    }
}

impl Display for ProgramInstruction {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> Result<(), std::fmt::Error> {
        write!(f, "ProgramInstruction()")
    }
}
