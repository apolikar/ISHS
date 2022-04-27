package irl.lyit.DublinSmartHouseSearch.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
public interface HouseRepository extends JpaRepository<House, Long> {

    @Transactional
    void deleteByUpdateTimeLessThan(long time);
}
