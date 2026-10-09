# Known Limitations

Keep this list honest and current.

- Real-time chat, Kafka, and AI features are planned; see [status.md](status.md) for what is built
- Matching quality has not been evaluated on a labelled set
- Single-region, single-instance deployment; no horizontal scaling validated
- WebSocket scaling beyond one node needs a broker relay (not yet designed in detail)
- AI output depends on a third-party model; results may vary and can be unavailable
- No end-to-end encryption for chat
- No automated moderation or abuse reporting yet
- WebRTC, file sharing, and repository verification are post-MVP
