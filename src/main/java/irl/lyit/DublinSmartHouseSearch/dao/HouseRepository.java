package irl.lyit.DublinSmartHouseSearch.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HouseRepository extends JpaRepository<House, Long> {

    void deleteByUpdateTimeLessThan(long time);
}
