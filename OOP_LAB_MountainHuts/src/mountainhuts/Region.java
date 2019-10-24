package mountainhuts;

import static java.util.stream.Collectors.toList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Region {
	
	private String Name;
	private String[] Altitudes;
	
	private TreeSet<Municipality> Muni = new TreeSet<Municipality>(new Comparator<Municipality>() {
		public int compare(Municipality a, Municipality b) {
			return a.getName().compareTo(b.getName());
		}
	});
	
	private TreeSet<MountainHut> MountainHut = new TreeSet<MountainHut>(new Comparator<MountainHut>() {
		public int compare(MountainHut a, MountainHut b) {
			return a.getName().compareTo(b.getName());
		}
	});
//	Map<Integer, Integer> Altitudes1 = new HashMap<Integer, Integer>();

	/**
	 * Create a region with the given name.
	 * 
	 * @param name
	 *            the name of the region
	 */
	public Region(String name) {
		this.Name = name;
	}

	/**
	 * Return the name of the region.
	 * 
	 * @return the name of the region
	 */
	public String getName() {
		return this.Name;
	}

	/**
	 * Create the ranges given their textual representation in the format
	 * "[minValue]-[maxValue]".
	 * 
	 * @param ranges
	 *            an array of textual ranges
	 */
	public void setAltitudeRanges(String... ranges) {
		this.Altitudes = ranges.clone();
	}

	/**
	 * Return the textual representation in the format "[minValue]-[maxValue]" of
	 * the range including the given altitude or return the default range "0-INF".
	 * 
	 * @param altitude
	 *            the geographical altitude
	 * @return a string representing the range
	 */
	public String getAltitudeRange(Integer altitude) {
	
		if(this.Altitudes!=null) {
			for(String str : this.Altitudes) {
				
				if (checkAltitude(str, altitude)) {
					return str;
				}
			}
		}
		
		return "0-INF";
	}

	/**
	 * Return all the municipalities available.
	 * 
	 * @return a collection of municipalities
	 */
	public Collection<Municipality> getMunicipalities() {
		return this.Muni;
	}

	/**
	 * Return all the mountain huts available.
	 * 
	 * @return a collection of mountain huts
	 */
	public Collection<MountainHut> getMountainHuts() {
		return this.MountainHut;
	}

	/**
	 * Create a new municipality if it is not already available or find it.
	 * Duplicates must be detected by comparing the municipality names.
	 * 
	 * @param name
	 *            the municipality name
	 * @param province
	 *            the municipality province
	 * @param altitude
	 *            the municipality altitude
	 * @return the municipality
	 */
	public Municipality createOrGetMunicipality(String name, String province, Integer altitude) {
		Municipality m = new Municipality(name, province, altitude);
		
		if(this.Muni.add(m)) {
			return m;
		}
		
		for(Municipality tmp : this.Muni) {
			if(tmp.getName().compareTo(name)==0) {
				return tmp;
			}
		}
		
//		return this.Muni.stream().filter(x -> x.getName().equals(name)).findFirst().orElseGet(() -> {
//			Municipality m = new Municipality(name, province, altitude);
//			Muni.add(m);
//			return m;
//		});
		
		return null;
	}

	/**
	 * Create a new mountain hut if it is not already available or find it.
	 * Duplicates must be detected by comparing the mountain hut names.
	 *
	 * @param name
	 *            the mountain hut name
	 * @param category
	 *            the mountain hut category
	 * @param bedsNumber
	 *            the number of beds in the mountain hut
	 * @param municipality
	 *            the municipality in which the mountain hut is located
	 * @return the mountain hut
	 */
	public MountainHut createOrGetMountainHut(String name, String category, Integer bedsNumber,
			Municipality municipality) {
		MountainHut m = new MountainHut(name, null, category, bedsNumber, municipality);
		
		if(this.MountainHut.add(m)) {
			return m;
		}
		
		for(MountainHut tmp : this.MountainHut) {
			if(tmp.getName().compareTo(name)==0) {
				return tmp;
			}
		}
		
		return null;
		
//		return this.MountainHut.stream().filter(x -> x.getName().equals(name)).findFirst().orElseGet(() -> {
//			MountainHut m = new MountainHut(name, null, category, bedsNumber, municipality);
//			MountainHut.add(m);
//			return m;
//		});
	}

	/**
	 * Create a new mountain hut if it is not already available or find it.
	 * Duplicates must be detected by comparing the mountain hut names.
	 * 
	 * @param name
	 *            the mountain hut name
	 * @param altitude
	 *            the mountain hut altitude
	 * @param category
	 *            the mountain hut category
	 * @param bedsNumber
	 *            the number of beds in the mountain hut
	 * @param municipality
	 *            the municipality in which the mountain hut is located
	 * @return a mountain hut
	 */
	public MountainHut createOrGetMountainHut(String name, Integer altitude, String category, Integer bedsNumber,
			Municipality municipality) {
		MountainHut m = new MountainHut(name, altitude, category, bedsNumber, municipality);
		
		if(this.MountainHut.add(m)) {
			return m;
		}
		
		for(MountainHut tmp : this.MountainHut) {
			if(tmp.getName().compareTo(name)==0) {
				return tmp;
			}
		}
		
		return null;
//		return this.MountainHut.stream().filter(x -> x.getName().equals(name)).findFirst().orElseGet(() -> {
//			MountainHut m = new MountainHut(name, altitude, category, bedsNumber, municipality);
//			MountainHut.add(m);
//			return m;
//		});
	}

	/**
	 * Creates a new region and loads its data from a file.
	 * 
	 * The file must be a CSV file and it must contain the following fields:
	 * <ul>
	 * <li>{@code "Province"},
	 * <li>{@code "Municipality"},
	 * <li>{@code "MunicipalityAltitude"},
	 * <li>{@code "Name"},
	 * <li>{@code "Altitude"},
	 * <li>{@code "Category"},
	 * <li>{@code "BedsNumber"}
	 * </ul>
	 * 
	 * The fields are separated by a semicolon (';'). The field {@code "Altitude"}
	 * may be empty.
	 * 
	 * @param name
	 *            the name of the region
	 * @param file
	 *            the path of the file
	 */
	public static Region fromFile(String name, String file) {
		Region r = new Region(name);
		
		r.readData(file);
		
		return r;
	}

	@SuppressWarnings("unused")
	private void readData(String file) {
		List<String> lines = null;

		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			lines = in.lines().collect(toList());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		if (lines == null)
			return;

		// Continue the implementation here
		int size = lines.get(0).split(";").length;
		String[] tmp = new String[size];
		
		for(int i=1; i<lines.size(); i++) {
				tmp = lines.get(i).split(";");
				
//				if(tmp[4].equals(""))
//				System.out.println("OK");
				
				Municipality m = this.createOrGetMunicipality(tmp[1], tmp[0], Integer.valueOf(tmp[2]));
				
				this.createOrGetMountainHut(tmp[3], tmp[4].equals("") ? null : Integer.valueOf(tmp[4]), tmp[5], Integer.valueOf(tmp[6]), m);
		}
	}

	/**
	 * Count the number of municipalities with at least a mountain hut per each
	 * province.
	 * 
	 * @return a map with the province as key and the number of municipalities as
	 *         value
	 */
	public Map<String, Long> countMunicipalitiesPerProvince() {
		
		Map<String, Long> map = new HashMap<>();
		Set<String> set = new HashSet<>();
		//System.out.println(this.Muni.stream().collect(Collectors.groupingBy(m -> m.getProvince())));
		
		for(Municipality mu : this.Muni) {
			set.add(mu.getProvince());
		}
		
		long count = 0;
		for(String str : set) {
			count = 0;
			for(Municipality mu : this.Muni) {
				if(mu.getProvince().equals(str)) {
					count++;
				}
			}
			map.put(str, count);
		}
		
		return map;
//		return this.Muni.stream().collect(Collectors.groupingBy(x -> x.getProvince(), Collectors.counting()));   //la lambda si può sostituire con una method reference: Municipality::getProvince
	}

	/**
	 * Count the number of mountain huts per each municipality within each province.
	 * 
	 * @return a map with the province as key and, as value, a map with the
	 *         municipality as key and the number of mountain huts as value
	 */
	public Map<String, Map<String, Long>> countMountainHutsPerMunicipalityPerProvince() {
		
		Map<String, Map<String, Long>> map = new HashMap<>();
		Set<String> setProv = new HashSet<>();
		Set<String> setMun = new HashSet<>();
		
		for(Municipality mu : this.Muni) {
			setProv.add(mu.getProvince());
			setMun.add(mu.getName());
		}
		
		long count = 0;
		for(String str : setProv) {
			Map<String, Long> mapVet = new HashMap<String, Long>();
			for(String str1 : setMun) {
				count = 0;
				Municipality m1 = null;
				for(Municipality m2 : this.Muni) {
					if(m2.getName().equals(str1)) {
						m1 = m2;
						break;
					}
				}
				if(m1.getProvince().equals(str)) {
					for(MountainHut mu : this.MountainHut) {
						if(mu.getMunicipality().getName().equals(str1) && mu.getMunicipality().getProvince().equals(str)) {
							count++;
						}
					}
					mapVet.put(str1, count);
				}	
			}
			map.put(str, mapVet);
		}
		
		return map;
//		return this.MountainHut.stream().collect(Collectors.groupingBy(x -> x.getMunicipality().getProvince(), 
//						Collectors.groupingBy(x -> x.getMunicipality().getName(), Collectors.counting())));
	}

	/**
	 * Count the number of mountain huts per altitude range. If the altitude of the
	 * mountain hut is not available, use the altitude of its municipality.
	 * 
	 * @return a map with the altitude range as key and the number of mountain huts
	 *         as value
	 */
	public Map<String, Long> countMountainHutsPerAltitudeRange() {
		
		Map<String, Long> map = new HashMap<>();
		long count=0;
		
		for(String str : this.Altitudes) {
			count=0;
			for(MountainHut m : this.MountainHut) {
				if(m.getAltitude().isPresent()) {
					if(checkAltitude(str, m.getAltitude().get())) {
						count++;
					}
				}
				else {
					if(checkAltitude(str, m.getMunicipality().getAltitude())) {
						count++;
					}
				}
			}
			map.put(str, count);
		}
		
		//ciclo per 0-INF
		long cINF = 0;
		for(MountainHut m : this.MountainHut) {
			count = 0;
			for(String str : this.Altitudes) {
				if(m.getAltitude().isPresent()) {
					if(checkAltitude(str, m.getAltitude().get())) {
						count++;
					}
				}
				else {
					if(checkAltitude(str, m.getMunicipality().getAltitude())) {
						count++;
					}
				}
			}
			if(count == 0) {
				cINF++;
			}
		}
		map.put("0-INF", cINF);
		
		return map;
//		return this.MountainHut.stream().collect(Collectors.groupingBy( (x) -> {
//			Integer altitude = null;
//			String tmp = "";
//			if( !x.getAltitude().isPresent()) {
//				altitude = x.getMunicipality().getAltitude();
//			}
//			else {
//				altitude = x.getAltitude().get();
//			}
//			for(String str : Altitudes) {
//				if(checkAltitude(str, altitude)) {
//					tmp = str;
//				}
//			}
//			return tmp;
//		}, Collectors.counting()));
	}

	/**
	 * Compute the total number of beds available in the mountain huts per each
	 * province.
	 * 
	 * @return a map with the province as key and the total number of beds as value
	 */
	public Map<String, Integer> totalBedsNumberPerProvince() {
		
		Map<String, Integer> map = new HashMap<>();
		Set<String> set = new HashSet<>();
		
		
		for(Municipality mu : this.Muni) {
			set.add(mu.getProvince());
		}
		
		int count=0;
		for(String str : set) {
			count=0;
			for(MountainHut m : this.MountainHut) {
				if(m.getMunicipality().getProvince().equals(str)) {
					count += m.getBedsNumber();
				}
			}
			map.put(str, count);
		}
		
		return map;
//		return this.MountainHut.stream().collect(Collectors.groupingBy(x -> x.getMunicipality().getProvince(), 
//				Collectors.summingInt(x -> x.getBedsNumber())));
//		
	}

	/**
	 * Compute the maximum number of beds available in a single mountain hut per
	 * altitude range. If the altitude of the mountain hut is not available, use the
	 * altitude of its municipality.
	 * 
	 * @return a map with the altitude range as key and the maximum number of beds
	 *         as value
	 */
	public Map<String, Optional<Integer>> maximumBedsNumberPerAltitudeRange() {
		
		Map<String, Optional<Integer>> map = new HashMap<>();
		
		Optional<Integer> max = Optional.empty();
		Optional<Integer> max0INF = Optional.empty();
		
		for(String str : this.Altitudes) {
			max = Optional.empty();
			for(MountainHut m : this.MountainHut) {
				if(m.getAltitude().isPresent()) {
					if(checkAltitude(str, m.getAltitude().get())) {
						if(max.isPresent()) {
							if(m.getBedsNumber() > max.get()) {
								max = Optional.of(m.getBedsNumber());
							}
						}
						else {
							max = Optional.of(m.getBedsNumber());
						}
					}
					else {
						if(max0INF.isPresent()) {
							if(m.getBedsNumber() > max0INF.get()) {
								max0INF = Optional.of(m.getBedsNumber());
							}
						}
						else {
							max0INF = Optional.of(m.getBedsNumber());
						}
					}
				}
				else {
					if(checkAltitude(str, m.getMunicipality().getAltitude())) {
						if(max.isPresent()) {
							if(m.getBedsNumber() > max.get()) {
								max = Optional.of(m.getBedsNumber());
							}
						}
						else {
							max = Optional.of(m.getBedsNumber());
						}
					}
					else {
						if(max0INF.isPresent()) {
							if(m.getBedsNumber() > max0INF.get()) {
								max0INF = Optional.of(m.getBedsNumber());
							}
						}
						else {
							max0INF = Optional.of(m.getBedsNumber());
						}
					}
				}
			}
			map.put(str, max);
		}
		map.put("0-INF", max0INF);
		
		return map;
//		return this.MountainHut.stream().collect(Collectors.groupingBy((x) -> {
//			Integer altitude = null;
//			String tmp = "";
//			if( !x.getAltitude().isPresent()) {
//				altitude = x.getMunicipality().getAltitude();
//			}
//			else {
//				altitude = x.getAltitude().get();
//			}
//			for(String str : Altitudes) {
//				if(checkAltitude(str, altitude)) {
//					tmp = str;
//				}
//			}
//			return tmp;
//			}, Collectors.mapping(x -> x.getBedsNumber(), Collectors.maxBy((a, b) -> a-b))));
	}

	/**
	 * Compute the municipality names per number of mountain huts in a municipality.
	 * The lists of municipality names must be in alphabetical order.
	 * 
	 * @return a map with the number of mountain huts in a municipality as key and a
	 *         list of municipality names as value
	 */
	public Map<Long, List<String>> municipalityNamesPerCountOfMountainHuts() {
		
		Map<Long, List<String>> map = new HashMap<>();
		Set<Long> set = new HashSet<>();
		
		long count=0;
		for(Municipality m : this.Muni) {
			count = 0;
			for(MountainHut mu : this.MountainHut) {
				if(mu.getMunicipality().getName().equals(m.getName())) {
					count++;
				}
			}
			set.add(count);
		}
		
		for(long l : set) {
			List<String> list = new ArrayList<>();
			for(Municipality m : this.Muni) {
				long c = 0;
				for(MountainHut mu : this.MountainHut) {
					if(mu.getMunicipality().getName().equals(m.getName())) {
						c++;
					}
				}
				if(c == l) {
					list.add(m.getName());
				}
			}
			list.sort(new Comparator<String>() {
				public int compare(String a, String b) {
					return a.compareTo(b);
				}
			});
			map.put(l, list);
		}
		
		//System.out.println(map);
			
		return map;
//		return this.MountainHut.stream().map(x -> x.getMunicipality().getName()).collect(Collectors.groupingBy(x -> x, Collectors.counting()))
//				.entrySet().stream().collect(Collectors.groupingBy(x -> x.getValue(), Collectors.mapping(x -> x.getKey(), toList())));
	}
	
	/**
	 * Controlla che l'intero passato si compreso nell'intervallo passato come string
	 * 
	 * @param str  intervallo di altitudini
	 * @param c   altitudine da testare
	 * @return
	 */
	public static boolean checkAltitude(String str, int c) {
		int a, b;
		int ind = str.indexOf("-");
		
		String st1 = str.substring(0, ind);
		String st2 = str.substring(ind+1, str.length());
		
		a = Integer.parseInt(st1);
		b = Integer.parseInt(st2);
		
		return (c>=a && c<=b) ;
	}

}
