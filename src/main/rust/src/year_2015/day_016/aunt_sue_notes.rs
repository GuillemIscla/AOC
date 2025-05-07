use regex::Regex;
use std::io::{self, Error, ErrorKind};

#[derive(Debug)]
pub struct AuntSueNotes {
    pub number: usize,
    pub children: Option<usize>,
    pub cats: Option<usize>,
    pub samoyeds: Option<usize>,
    pub pomeranians: Option<usize>,
    pub akitas: Option<usize>,
    pub vizslas: Option<usize>,
    pub goldfish: Option<usize>,
    pub trees: Option<usize>,
    pub cars: Option<usize>,
    pub perfumes: Option<usize>,
}

impl AuntSueNotes {
    pub fn from_raw(raw: &str) -> io::Result<AuntSueNotes> {
        let regex_1_raw = r"Sue ([0-9]+):";
        let regex_1 =
            Regex::new(regex_1_raw).map_err(|e| Error::new(ErrorKind::InvalidInput, e))?;
        let regex_2_raw = r"(\w+): ([0-9]+)";
        let regex_2 =
            Regex::new(regex_2_raw).map_err(|e| Error::new(ErrorKind::InvalidInput, e))?;

        let captures_1 = regex_1
            .captures(raw)
            .ok_or_else(|| Error::new(ErrorKind::InvalidData, format!("Invalid line: {}", raw)))?;

        let captures_2 = regex_2.captures_iter(raw);

        let number = captures_1[1]
            .parse::<usize>()
            .map_err(|e| Error::new(ErrorKind::InvalidData, e))?;

        let mut aunt_sue_notes = AuntSueNotes {
            number,
            children: None,
            cats: None,
            samoyeds: None,
            pomeranians: None,
            akitas: None,
            vizslas: None,
            goldfish: None,
            trees: None,
            cars: None,
            perfumes: None,
        };

        captures_2
            .map(|capture| {
                AuntSueNotes::parse_single_note(&capture[1], &capture[2], &mut aunt_sue_notes)
            })
            .collect::<io::Result<Vec<()>>>()?;

        Ok(aunt_sue_notes)
    }

    fn parse_single_note(
        note_label: &str,
        note_value: &str,
        aunt_sue_notes: &mut AuntSueNotes,
    ) -> io::Result<()> {
        let note_value_parsed = note_value
            .parse::<usize>()
            .map_err(|e| Error::new(ErrorKind::InvalidData, e))?;

        if note_label == "children" {
            aunt_sue_notes.children = Some(note_value_parsed);
        } else if note_label == "cats" {
            aunt_sue_notes.cats = Some(note_value_parsed);
        } else if note_label == "samoyeds" {
            aunt_sue_notes.samoyeds = Some(note_value_parsed);
        } else if note_label == "pomeranians" {
            aunt_sue_notes.pomeranians = Some(note_value_parsed);
        } else if note_label == "akitas" {
            aunt_sue_notes.akitas = Some(note_value_parsed);
        } else if note_label == "vizslas" {
            aunt_sue_notes.vizslas = Some(note_value_parsed);
        } else if note_label == "goldfish" {
            aunt_sue_notes.goldfish = Some(note_value_parsed);
        } else if note_label == "trees" {
            aunt_sue_notes.trees = Some(note_value_parsed);
        } else if note_label == "cars" {
            aunt_sue_notes.cars = Some(note_value_parsed)
        } else if note_label == "perfumes" {
            aunt_sue_notes.perfumes = Some(note_value_parsed);
        } else {
            return Err(Error::new(
                ErrorKind::InvalidData,
                format!("Non-valid label for note: '{}'", note_label),
            ));
        }

        Ok(())
    }
}
