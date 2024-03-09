package com.nikita.taskcreateserviceforcv.services;

import com.nikita.taskcreateserviceforcv.entities.Area;
import com.nikita.taskcreateserviceforcv.entities.Candidate;
import com.nikita.taskcreateserviceforcv.exceptions.BadRequestException;
import com.nikita.taskcreateserviceforcv.exceptions.NotFoundException;
import com.nikita.taskcreateserviceforcv.repositories.candidateRepositories.CandidateJpaRepository;
import com.nikita.taskcreateserviceforcv.repositories.candidateRepositories.CandidatePagingAndSortingRepository;
import com.nikita.taskcreateserviceforcv.services.interfaces.JpaService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CandidateService implements JpaService<Candidate, Long> {

    private final CandidateJpaRepository candidateJpaRepository;
    private final CandidatePagingAndSortingRepository candidatePagingAndSortingRepository;
    private final FileServiceImpl fileService;

    private final AreaService areaService;

    public Candidate findById(Long aLong) {
        return candidateJpaRepository.findById(aLong).orElseThrow(() ->
                new NotFoundException("Candidate with id %d not found".formatted(aLong)));
    }

    public Page<Candidate> findAll(Integer offset, Integer limit, String keyword, Boolean isSorted) {

        Specification<Candidate> spec = Specification.where(null);

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and((Root<Candidate> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
                Predicate lastnameLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastname")), "%" + keyword.toLowerCase() + "%");
                Predicate descriptionLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + keyword.toLowerCase() + "%");
                return criteriaBuilder.or(lastnameLike, descriptionLike);
            });
        }

        if (isSorted) {
            Sort sortByLastname = Sort.by(Sort.Direction.ASC, "lastname");
            return candidatePagingAndSortingRepository.findAll(spec, PageRequest.of(offset, limit, sortByLastname));
        }


        return candidatePagingAndSortingRepository.findAll(spec, PageRequest.of(offset, limit));
    }

    public boolean exists(Example<Candidate> of) {
        return candidateJpaRepository.exists(of);
    }

    @Transactional
    public <S extends Candidate> S save(S entity) {

        entity.setPossibleAreas(entity.getPossibleAreas().stream().map(area -> {

            Area byId;

            try {
                byId = areaService.findById(area.getId());
                byId.getCandidatesForArea().add(entity);
            } catch (Exception e) {
                throw new BadRequestException("Area with such id does not exist");
            }

            return byId;
        }).collect(Collectors.toSet()));

        return candidateJpaRepository.save(entity);
    }

    @Transactional
    public <S extends Candidate> S save(S entity, MultipartFile photo, MultipartFile cvFile) {


        String photoName = uploadFile(photo);
        String cvFileName = uploadFile(cvFile);

        entity.setPhoto(photoName);
        entity.setCvFile(cvFileName);

        log.info("saving {}", entity);
        return save(entity);
    }

    private String uploadFile(MultipartFile file) {
        return fileService.upload(file);
    }

    @Transactional
    public <S extends Candidate> S update(S entity) {
        return candidateJpaRepository.save(entity);
    }

    @Transactional
    public Candidate update(Candidate candidate, MultipartFile photo, MultipartFile cvFile) {

        updatePhoto(candidate, photo);
        updateCv(candidate, cvFile);

        log.info("updating {}", candidate);
        return update(candidate);
    }

    private void updatePhoto(Candidate candidate, MultipartFile photo) {
        if (photo.isEmpty() && candidate.getPhoto() != null) {
            fileService.rmFile(candidate.getPhoto());
            candidate.setPhoto(null);
            return;
        }

        if (!photo.isEmpty() && candidate.getPhoto() == null) {
            String photoName = uploadFile(photo);
            candidate.setPhoto(photoName);
            return;
        }

        if (!photo.isEmpty() && candidate.getPhoto() != null) {
            boolean b = fileService.eqFile(candidate.getPhoto(), photo);
            if (!b) {
                fileService.rmFile(candidate.getPhoto());
                String photoName = uploadFile(photo);
                candidate.setPhoto(photoName);
            }
        }
    }

    private void updateCv(Candidate candidate, MultipartFile cvFile) {
        if (cvFile.isEmpty() && candidate.getCvFile() != null) {
            fileService.rmFile(candidate.getCvFile());
            candidate.setCvFile(null);
            return;
        }

        if (!cvFile.isEmpty() && candidate.getCvFile() == null) {
            String cvName = uploadFile(cvFile);
            candidate.setCvFile(cvName);
            return;
        }

        if (!cvFile.isEmpty() && candidate.getCvFile() != null) {
            boolean b = fileService.eqFile(candidate.getCvFile(), cvFile);
            if (!b) {
                fileService.rmFile(candidate.getCvFile());
                String cvName = uploadFile(cvFile);
                candidate.setCvFile(cvName);
            }
        }
    }
}
