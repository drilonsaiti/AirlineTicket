package airlanetickets.repository;

import airlanetickets.model.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;


public interface FlightRepository extends JpaRepository<Flight,Long> , PagingAndSortingRepository<Flight,Long> {

    Page<Flight> findAllByFromLocationLikeAndToLocationLikeAndDeparatureTimeLike(String fromSearch, String toSearch, String deptSearch, Pageable pageable);

    Page<Flight> findAllByFromLocationLikeAndToLocationLike(String fromSearch,String toSearch,Pageable pageable);

    Page<Flight> findAllByFromLocationLikeAndDeparatureTimeLike(String fromSearch,String toSearch,Pageable pageable );

    Page<Flight> findAllByToLocationLikeAndDeparatureTimeLike(String toSearch,String deptSearch,Pageable pageable);

    Page<Flight> findAllByFromLocationLike(String fromSearch ,Pageable pageable);

    Page<Flight> findAllByToLocationLike(String deptSearch,Pageable pageable);

    Page<Flight> findAllByDeparatureTimeLike(String deptSearch,Pageable pageable);

    Optional<Flight> findById(Long id);
}
