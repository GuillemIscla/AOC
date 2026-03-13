use crate::year_2025::day_011::device::Device;
use crate::year_2025::day_011::path_checkpoint_counter::PathCheckpointCounter;
use std::collections::HashMap;

pub fn path_count_deep_first(
    origin: String,
    devices: &[Device],
    cache: &mut HashMap<String, u32>,
) -> u32 {
    match &cache.get(&origin) {
        Some(cached_value) => **cached_value,
        None => devices
            .iter()
            .find_map(|device| {
                if device.id == origin {
                    Some(device.go_to.clone())
                } else {
                    None
                }
            })
            .unwrap()
            .iter()
            .map(|next_device| {
                let partial_result = path_count_deep_first(next_device.to_string(), devices, cache);
                cache.insert(next_device.to_string(), partial_result);
                partial_result
            })
            .sum(),
    }
}

pub fn path_checkpoint_count_deep_first(
    origin: String,
    devices: &[Device],
    cache: &mut HashMap<String, PathCheckpointCounter>,
) -> PathCheckpointCounter {
    match &cache.get(&origin) {
        Some(cached_value) => (**cached_value).clone(),
        None => {
            let result: PathCheckpointCounter = devices
                .iter()
                .find_map(|device| {
                    if device.id == origin {
                        Some(device.go_to.clone())
                    } else {
                        None
                    }
                })
                .unwrap()
                .into_iter()
                .map(|next_device| {
                    let partial_result =
                        path_checkpoint_count_deep_first(next_device.to_string(), devices, cache)
                            .clone();
                    match origin.as_str() {
                        "fft" => PathCheckpointCounter {
                            only_fft: partial_result.neither,
                            only_dac: 0,
                            both: partial_result.only_dac,
                            neither: 0,
                        },
                        "dac" => PathCheckpointCounter {
                            only_fft: 0,
                            only_dac: partial_result.neither,
                            both: partial_result.only_fft,
                            neither: 0,
                        },
                        _ => partial_result,
                    }
                })
                .sum();
            cache.insert(origin, result.clone());
            result
        }
    }
}
