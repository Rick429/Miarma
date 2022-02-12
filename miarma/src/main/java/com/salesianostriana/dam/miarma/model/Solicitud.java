package com.salesianostriana.dam.miarma.model;

import com.salesianostriana.dam.miarma.users.model.UserEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Solicitud implements Serializable {

    @EmbeddedId
    @Builder.Default
    private SolicitudPK id = new SolicitudPK();

    @ManyToOne
    @MapsId("solicitante_id")
    @JoinColumn(name = "solicitante_id")
    private UserEntity solicitante;

    @ManyToOne
    @MapsId("solicitado_id")
    @JoinColumn(name = "solicitado_id")
    private UserEntity solicitado;

    @CreatedDate
    private LocalDateTime createdDate;

    /* HELPERS */

    public void addToSolicitado(UserEntity s) {
        solicitado = s;
        s.getSolicitados().add(this);
    }

    public void removeFromSolicitado(UserEntity s) {
        s.getSolicitados().remove(this);
        solicitado = null;
    }

    public void addToSolicitante(UserEntity sol) {
        solicitante = sol;
        sol.getSolicitudes().add(this);
    }

    public void removeFromSolicitante(UserEntity sol) {
        sol.getSolicitudes().remove(this);
        solicitante = null;
    }

    public void addSolicitadoToSolicitante(UserEntity solicitado, UserEntity solicitante) {
        addToSolicitado(solicitado);
        addToSolicitante(solicitante);
    }

    public void removeSolicitadoToSolicitante(UserEntity solicitado, UserEntity solicitante) {
        removeFromSolicitado(solicitado);
        removeFromSolicitante(solicitante);
    }

}
